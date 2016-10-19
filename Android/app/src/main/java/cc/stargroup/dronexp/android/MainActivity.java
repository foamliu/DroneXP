package cc.stargroup.dronexp.android;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.dji.videostreamdecodingsample.media.DJIVideoStreamDecoder;
import dji.sdk.Battery.DJIBattery;
import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICamera.CameraReceivedVideoDataCallback;
import dji.sdk.Camera.DJICameraSettingsDef;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.Products.DJIAircraft;
import dji.sdk.RemoteController.DJIRemoteController;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;


public class MainActivity extends Activity implements View.OnClickListener, DJIVideoStreamDecoder.IYuvDataListener {

    private static final String TAG = MainActivity.class.getName();
    private Logger logger = new Logger();
    protected CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;

    private TextureView mVideoSurface = null;
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceTextureListener mSurfaceListener;
    private SensorManager mSensorManager;
    private ActuatorManager mActuator;

    public DJIFlightController mFlightController;
    public DJIRemoteController mRemoteController;
    public DJIGimbal mGimbal;
    public DJIBattery mBattery;

    private Timer mFeedbackLoopTimer;
    private FeedbackLoopTask mFeedbackLoopTask;

    String timeString;
    boolean isVideoRecording = false;
    boolean mIsControlled = false;

    //Button mRecordVideoModeBtn;
    ToggleButton mRecordBtn, mControlBtn;
    //ToggleButton mSafeModeBtn;
    Button mTakeOffBtn, mAutoLandingBtn, mGoHomeBtn;

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
                    //mSurfaceListener.getCodecManager().sendDataToDecoder(videoBuffer, size);
                    DJIVideoStreamDecoder.getInstance().parse(videoBuffer, size);
                    //logger.appendLog("camera recv video data size: " + size);
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
        if (mActuator == null) {
            mActuator = new ActuatorManager(this);
        }
        if (mSensorManager == null) {
            mSensorManager = new SensorManager(this, mActuator);
        }

        mSensorManager.registerListener();

        super.onResume();
    }

    @Override
    public void onPause() {
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
        mVideoSurface = (TextureView) findViewById(R.id.texture_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                DJIVideoStreamDecoder.getInstance().init(getApplicationContext(), mSurfaceHolder.getSurface());
                DJIVideoStreamDecoder.getInstance().setYuvDataListener(MainActivity.this);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                DJIVideoStreamDecoder.getInstance().changeSurface(holder.getSurface());
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

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

        //mRecordVideoModeBtn = (Button) findViewById(R.id.btn_record_video_mode);
        //mRecordVideoModeBtn.setOnClickListener(this);

        mControlBtn = (ToggleButton) findViewById(R.id.btn_control);
        mControlBtn.setOnClickListener(this);
        mControlBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableControl();
                } else {
                    disableControl();
                }
            }
        });

        mTakeOffBtn = (Button) findViewById(R.id.btn_take_off);
        mTakeOffBtn.setOnClickListener(this);

        mAutoLandingBtn = (Button) findViewById(R.id.btn_auto_landing);
        mAutoLandingBtn.setOnClickListener(this);

//        mSafeModeBtn = (ToggleButton) findViewById(R.id.btn_safe_mode);
//        mSafeModeBtn.setOnClickListener(this);
//        mSafeModeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    normalMode();
//                } else {
//                    safeMode();
//                }
//            }
//        });

        mGoHomeBtn = (Button) findViewById(R.id.btn_go_home);
        mGoHomeBtn.setOnClickListener(this);
    }

    private void initTimer() {
        if (null == mFeedbackLoopTimer) {
            mFeedbackLoopTask = new FeedbackLoopTask();
            mFeedbackLoopTimer = new Timer();
            mFeedbackLoopTimer.schedule(mFeedbackLoopTask, 0, 100);
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
            mBattery = aircraft.getBattery();

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_go_home:{
                mActuator.goHome();
                break;
            }
            case R.id.btn_take_off: {
                mActuator.takeOff();
                break;
            }
            case R.id.btn_auto_landing: {
                mActuator.autoLanding();
                break;
            }
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

    public boolean isControlled() {
        return mIsControlled;
    }

    // Method for starting recording
    private void startRecord() {

        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.RecordVideo;
        switchCameraMode(cameraMode);

        final DJICamera camera = DroneXPApplication.getCameraInstance();
        if (camera != null) {
            camera.startRecordVideo(new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        showToast("Record video: success");
                    } else {
                        showToast(error.getDescription());
                    }
                }
            }); // Execute the startRecordVideo API
        }
    }

    // Method for stopping recording
    private void stopRecord() {

        DJICamera camera = DroneXPApplication.getCameraInstance();
        if (camera != null) {
            camera.stopRecordVideo(new DJIBaseComponent.DJICompletionCallback() {

                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        showToast("Stop recording: success");
                    } else {
                        showToast(error.getDescription());
                    }
                }
            }); // Execute the stopRecordVideo API
        }

    }

    private void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode) {

        DJICamera camera = DroneXPApplication.getCameraInstance();
        if (camera != null) {
            camera.setCameraMode(cameraMode, new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {

                    if (error == null) {
                        showToast("Switch Camera Mode Succeeded");
                    } else {
                        showToast(error.getDescription());
                    }
                }
            });
        }

    }

    private void enableControl() {
        mIsControlled = true;
    }

    private void disableControl() {
        mIsControlled = false;
    }

//    private void normalMode() {
//        if (mActuator != null) {
//            mActuator.setSafeLimit(1.0F);
//        }
//    }
//
//    private void safeMode() {
//        if (mActuator != null) {
//            mActuator.setSafeLimit(0.1F);
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onYuvDataReceived(byte[] yuvFrame, int width, int height) {
        //In this demo, we test the YUV data by saving it into JPG files.
        if (DJIVideoStreamDecoder.getInstance().frameIndex % 30 == 0) {
            byte[] y = new byte[width * height];
            byte[] u = new byte[width * height / 4];
            byte[] v = new byte[width * height / 4];
            byte[] nu = new byte[width * height / 4]; //
            byte[] nv = new byte[width * height / 4];
            System.arraycopy(yuvFrame, 0, y, 0, y.length);
            for (int i = 0; i < u.length; i++) {
                v[i] = yuvFrame[y.length + 2 * i];
                u[i] = yuvFrame[y.length + 2 * i + 1];
            }
            int uvWidth = width / 2;
            int uvHeight = height / 2;
            for (int j = 0; j < uvWidth / 2; j++) {
                for (int i = 0; i < uvHeight / 2; i++) {
                    byte uSample1 = u[i * uvWidth + j];
                    byte uSample2 = u[i * uvWidth + j + uvWidth / 2];
                    byte vSample1 = v[(i + uvHeight / 2) * uvWidth + j];
                    byte vSample2 = v[(i + uvHeight / 2) * uvWidth + j + uvWidth / 2];
                    nu[2 * (i * uvWidth + j)] = uSample1;
                    nu[2 * (i * uvWidth + j) + 1] = uSample1;
                    nu[2 * (i * uvWidth + j) + uvWidth] = uSample2;
                    nu[2 * (i * uvWidth + j) + 1 + uvWidth] = uSample2;
                    nv[2 * (i * uvWidth + j)] = vSample1;
                    nv[2 * (i * uvWidth + j) + 1] = vSample1;
                    nv[2 * (i * uvWidth + j) + uvWidth] = vSample2;
                    nv[2 * (i * uvWidth + j) + 1 + uvWidth] = vSample2;
                }
            }
            //nv21test
            byte[] bytes = new byte[yuvFrame.length];
            System.arraycopy(y, 0, bytes, 0, y.length);
            for (int i = 0; i < u.length; i++) {
                bytes[y.length + (i * 2)] = nv[i];
                bytes[y.length + (i * 2) + 1] = nu[i];
            }
            String msg =  "onYuvDataReceived: frame index: "
                    + DJIVideoStreamDecoder.getInstance().frameIndex
                    + ",array length: "
                    + bytes.length;
            Log.d(TAG,msg);
            logger.appendLog(msg);
            screenShot(bytes, Environment.getExternalStorageDirectory() + "/DJI_ScreenShot");
        }
    }

    /**
     * Save the buffered data into a JPG image file
     */
    private void screenShot(byte[] buf, String shotDir) {
        File dir = new File(shotDir);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        YuvImage yuvImage = new YuvImage(buf,
                ImageFormat.NV21,
                DJIVideoStreamDecoder.getInstance().width,
                DJIVideoStreamDecoder.getInstance().height,
                null);
        OutputStream outputFile;
        final String path = dir + "/ScreenShot_" + System.currentTimeMillis() + ".jpg";
        try {
            outputFile = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            String msg = "test screenShot: new bitmap output file error: " + e;
            Log.e(TAG, msg);
            logger.appendLog(msg);
            return;
        }
        if (outputFile != null) {
            yuvImage.compressToJpeg(new Rect(0,
                    0,
                    DJIVideoStreamDecoder.getInstance().width,
                    DJIVideoStreamDecoder.getInstance().height), 100, outputFile);
        }
        try {
            outputFile.close();
        } catch (IOException e) {
            String msg = "test screenShot: compress yuv image error: " + e;
            Log.e(TAG, msg);
            logger.appendLog(msg);
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(path);
            }
        });
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


