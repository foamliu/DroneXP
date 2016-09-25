package cc.stargroup.dronexp.android;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICamera.CameraReceivedVideoDataCallback;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.Products.DJIAircraft;
import dji.sdk.RemoteController.DJIRemoteController;


public class MainActivity extends Activity  {

    private static final String TAG = MainActivity.class.getName();
    private Logger logger = new Logger();
    protected CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;

    protected TextureView mLeftVideoSurface = null, mRightVideoSurface = null;
    private SurfaceTextureListener mLeftSurfaceListener, mRightSurfaceListener;
    private SensorManager mSensorManager = new SensorManager(this);

    public DJIFlightController mFlightController;
    public DJIRemoteController mRemoteController;

    private Timer mFeedbackLoopTimer;
    private FeedbackLoopTask mFeedbackLoopTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initPermissions();
        setContentView(R.layout.activity_main);

        mLeftSurfaceListener = new SurfaceTextureListener(this);
        mRightSurfaceListener = new SurfaceTextureListener(this);

        // The callback for receiving the raw H264 video data for camera live view
        mReceivedVideoDataCallBack = new CameraReceivedVideoDataCallback() {

            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if (mLeftSurfaceListener.getCodecManager() != null) {
                    // Send the raw H264 video data to codec manager for decoding
                    mLeftSurfaceListener.getCodecManager().sendDataToDecoder(videoBuffer, size);
                } else {
                    logger.appendLog("mLeftSurfaceListener.getCodecManager() is null");
                }

                if (mRightSurfaceListener.getCodecManager() != null) {
                    // Send the raw H264 video data to codec manager for decoding
                    mRightSurfaceListener.getCodecManager().sendDataToDecoder(videoBuffer, size);
                } else {
                    logger.appendLog("mRightSurfaceListener.getCodecManager() is null");
                }
            }
        };

        initUI();
        initTimer();

        // Register the broadcast receiver for receiving the device connection's changes.
        IntentFilter filter = new IntentFilter();
        filter.addAction(DroneXPApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);
    }

    private void initPermissions() {
        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }
    }

    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initFlightController();
        }
    };

    @Override
    public void onResume() {
        logger.appendLog("onResume");
        mSensorManager.Sense();

        super.onResume();
    }

    @Override
    public void onPause() {
        logger.appendLog("onPause");

        uninitPreviewer();
        super.onPause();
    }

    @Override
    public void onStop() {
        logger.appendLog("onStop");

        super.onStop();
    }

    public void onReturn(View view) {
        logger.appendLog("onReturn");

        this.finish();
    }

    @Override
    protected void onDestroy() {
        logger.appendLog("onDestroy");

        uninitPreviewer();
        super.onDestroy();
    }

    private void initUI() {
        logger.appendLog("initUI");
        // initFlightController mVideoSurface
        mLeftVideoSurface = (TextureView) findViewById(R.id.video_surface_left);
        mRightVideoSurface = (TextureView) findViewById(R.id.video_surface_right);

        setSurfaceListener();
    }

    private void initTimer() {
        if (null == mFeedbackLoopTimer) {
            mFeedbackLoopTask = new FeedbackLoopTask();
            mFeedbackLoopTimer = new Timer();
            mFeedbackLoopTimer.schedule(mFeedbackLoopTask, 0, 1000);
        }
    }

    private void setSurfaceListener() {
        if (null != mLeftVideoSurface) {
            mLeftVideoSurface.setSurfaceTextureListener(mLeftSurfaceListener);
        }
        if (null != mRightVideoSurface) {
            mRightVideoSurface.setSurfaceTextureListener(mRightSurfaceListener);
        }
    }

    private void initFlightController() {

        logger.appendLog("initFlightController");

        DJIAircraft aircraft = DroneXPApplication.getAircraftInstance();
        if (aircraft == null || !aircraft.isConnected()) {
            showToast("Disconnected");
            mFlightController = null;
            return;
        } else {
            mFlightController = aircraft.getFlightController();
            mRemoteController = aircraft.getRemoteController();

            logger.appendLog("aircraft is: " + aircraft.getModel().getDisplayName());
            setSurfaceListener();

            DJICamera camera = aircraft.getCamera();
            if (camera != null) {
                // Set the callback
                camera.setDJICameraReceivedVideoDataCallback(mReceivedVideoDataCallBack);
            }
        }
    }

    private void uninitPreviewer() {
        DJICamera camera = DroneXPApplication.getCameraInstance();
        if (camera != null) {
            // Reset the callback
            DroneXPApplication.getCameraInstance().setDJICameraReceivedVideoDataCallback(null);
        }
    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    class FeedbackLoopTask extends TimerTask {

        @Override
        public void run() {
            mSensorManager.Sense();

        }
    }
}


