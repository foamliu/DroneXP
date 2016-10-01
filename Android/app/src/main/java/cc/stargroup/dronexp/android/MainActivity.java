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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;

import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICamera.CameraReceivedVideoDataCallback;
import dji.sdk.Camera.DJICameraSettingsDef;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.Products.DJIAircraft;
import dji.sdk.RemoteController.DJIRemoteController;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;


public class MainActivity extends Activity implements View.OnClickListener {

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

    String timeString;
    boolean isVideoRecording;

    ToggleButton mRecordBtn;

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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        DJICamera camera = DroneXPApplication.getCameraInstance();

        if (camera != null) {
            camera.setDJICameraUpdatedSystemStateCallback(new DJICamera.CameraUpdatedSystemStateCallback() {
                @Override
                public void onResult(DJICamera.CameraSystemState cameraSystemState) {
                    if (null != cameraSystemState) {

                        int recordTime = cameraSystemState.getCurrentVideoRecordingTimeInSeconds();
                        int minutes = (recordTime % 3600) / 60;
                        int seconds = recordTime % 60;

                        timeString = String.format("%02d:%02d", minutes, seconds);
                        isVideoRecording = cameraSystemState.isRecording();
                    }
                }
            });
        }
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
        //joystick.Connect(this, null);

    }

    @Override
    public void onPause() {
        //logger.appendLog("onPause");
        //joystick.Disconnect();
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

        mRecordBtn = (ToggleButton) findViewById(R.id.btn_record);
        mRecordBtn.setOnClickListener(this);
        mRecordBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startRecord();
                } else {
                    stopRecord();
                }
            }
        });
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
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (this.mSensorManager.dispatchGenericMotionEvent(event))
            return true;
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mSensorManager.dispatchKeyEvent(event))
            return true;
        else
            return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Handle DPad keys and fire button on initial down but not on
        // auto-repeat.
        boolean handled = false;
        if (event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    handled = true;
                    break;
                default:
                    handled = true;
                    break;
            }
        }
        return handled;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // Handle keys going up.
        boolean handled = false;

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                handled = true;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                handled = true;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                handled = true;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                handled = true;
                break;
            default:
                handled = true;
                break;
        }

        return handled;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    public String getTimeString() {
        return timeString;
    }

    public boolean isVideoRecording() {
        return isVideoRecording;
    }

    // Method for starting recording
    private void startRecord(){

        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.RecordVideo;
        final DJICamera camera = DroneXPApplication.getCameraInstance();
        if (camera != null) {
            camera.startRecordVideo(new DJIBaseComponent.DJICompletionCallback(){
                @Override
                public void onResult(DJIError error)
                {
                    if (error == null) {
                        showToast("Record video: success");
                    }else {
                        showToast(error.getDescription());
                    }
                }
            }); // Execute the startRecordVideo API
        }
    }

    // Method for stopping recording
    private void stopRecord(){

        DJICamera camera = DroneXPApplication.getCameraInstance();
        if (camera != null) {
            camera.stopRecordVideo(new DJIBaseComponent.DJICompletionCallback(){

                @Override
                public void onResult(DJIError error)
                {
                    if(error == null) {
                        showToast("Stop recording: success");
                    }else {
                        showToast(error.getDescription());
                    }
                }
            }); // Execute the stopRecordVideo API
        }

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


