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
import android.widget.TextView;
import android.widget.Toast;

import com.baofeng.mojing.MojingVrActivity;
import com.baofeng.mojing.input.base.MojingInputCallback;

import java.util.Timer;
import java.util.TimerTask;

import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICamera.CameraReceivedVideoDataCallback;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.Products.DJIAircraft;
import dji.sdk.RemoteController.DJIRemoteController;


public class MainActivity extends MojingVrActivity implements MojingInputCallback {

    private static final String TAG = MainActivity.class.getName();
    private Logger logger = new Logger();
    protected CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;

    protected TextureView mVideoSurface = null;
    private SurfaceTextureListener mSurfaceListener;
    private SensorManager mSensorManager;

    public DJIFlightController mFlightController;
    public DJIRemoteController mRemoteController;
    public DJIGimbal mGimbal;

    private Timer mFeedbackLoopTimer;
    private FeedbackLoopTask mFeedbackLoopTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initPermissions();
        setContentView(R.layout.activity_main);

        mSurfaceListener = new SurfaceTextureListener(this);

        // The callback for receiving the raw H264 video data for camera live view
        mReceivedVideoDataCallBack = new CameraReceivedVideoDataCallback() {

            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if (mSurfaceListener.getCodecManager() != null) {
                    // Send the raw H264 video data to codec manager for decoding
                    mSurfaceListener.getCodecManager().sendDataToDecoder(videoBuffer, size);
                } else {
                    logger.appendLog("mSurfaceListener.getCodecManager() is null");
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
        if (mSensorManager == null) {
            mSensorManager = new SensorManager(this);
        }
        mSensorManager.registerListener();

        super.onResume();
    }

    @Override
    public void onPause() {
        logger.appendLog("onPause");
        uninitPreviewer();
        mSensorManager.unregisterListener();
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
        mVideoSurface = (TextureView) findViewById(R.id.video_surface);

        setSurfaceListener();
    }

    private void initTimer() {
        if (null == mFeedbackLoopTimer) {
            mFeedbackLoopTask = new FeedbackLoopTask();
            mFeedbackLoopTimer = new Timer();
            mFeedbackLoopTimer.schedule(mFeedbackLoopTask, 0, 200);
        }
    }

    private void setSurfaceListener() {
        if (null != mVideoSurface) {
            mVideoSurface.setSurfaceTextureListener(mSurfaceListener);
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
            mGimbal = aircraft.getGimbal();

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

    public void showText(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                TextView textView = (TextView) findViewById(R.id.text_info);
                textView.setText(msg);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onMojingKeyDown(String s, int i) {
        return false;
    }

    @Override
    public boolean onMojingKeyUp(String s, int i) {
        return false;
    }

    @Override
    public boolean onMojingKeyLongPress(String s, int i) {
        return false;
    }

    @Override
    public boolean onMojingMove(String s, int i, float v, float v1, float v2) {
        return false;
    }

    @Override
    public boolean onMojingMove(String s, int i, float v) {
        return false;
    }

    @Override
    public void onMojingDeviceAttached(String s) {

    }

    @Override
    public void onMojingDeviceDetached(String s) {

    }

    @Override
    public void onBluetoothAdapterStateChanged(int i) {

    }

    class FeedbackLoopTask extends TimerTask {

        @Override
        public void run() {
            if (mSensorManager != null) {
                mSensorManager.Sense();
            }
        }
    }
}


