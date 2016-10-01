package cc.stargroup.dronexp.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;

import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.RemoteController.DJIRemoteController;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by Foam on 2016/9/25.
 */
public class SensorManager implements SensorEventListener {
    static final String TAG = MainActivity.class.getName();
    Logger logger = new Logger();
    MainActivity mActivity;
    android.hardware.SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;
    float orientation[] = new float[3];

    ActuatorManager mActuator;

    public SensorManager(MainActivity activity) {
        this.mActivity = activity;
        this.mActuator = new ActuatorManager(activity);

        mSensorManager = (android.hardware.SensorManager)activity.getSystemService(activity.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void Sense() {
        double mobileYaw = Math.toDegrees(orientation[0]);
        double mobilePitch = Math.toDegrees(orientation[1]);
        final double mobileRoll = Math.toDegrees(orientation[2]);

        final StringBuilder str = new StringBuilder();
        str.append(String.format("Mobile -> yaw=%f pitch=%f roll=%f", mobileYaw, mobilePitch, mobileRoll));
        str.append(" " + calculateOrientation() + "\n");

        str.append("Joystick X-Axis: " + xaxis + ", Y-Axis" + yaxis + "\n");
        //logger.appendLog(str);

        //Log.i(TAG, str.toString());

        if (mActivity.mFlightController != null) {
            DJIFlightControllerDataType.DJIAttitude attitude = mActivity.mFlightController.getCurrentState().getAttitude();
            String yaw = String.format("%.2f", attitude.yaw);
            String pitch = String.format("%.2f", attitude.pitch);
            String roll = String.format("%.2f", attitude.roll);

            DJIFlightControllerDataType.DJILocationCoordinate3D location = mActivity.mFlightController.getCurrentState().getAircraftLocation();
            String altitude = String.format("%.2f", location.getAltitude());
            String latitude = String.format("%.2f", location.getLatitude());
            String longitude = String.format("%.2f", location.getLongitude());

            str.append("FlightController -> Yaw : " + yaw + ", Pitch : " + pitch + ", Roll : " + roll + "\n" + ", Altitude : " + altitude +
                    ", Latitude : " + latitude +
                    ", Longitude : " + longitude + "\n");
            //logger.appendLog(str);
        }

        if (mActivity.mGimbal != null) {
            DJIGimbal.DJIGimbalAttitude attitude = mActivity.mGimbal.getAttitudeInDegrees();
            str.append("Gimbal -> pitch : " + attitude.pitch + ", roll : " + attitude.roll + ", yaw : " + attitude.yaw + "\n");

            float dPitch = 90 + (float)mobileRoll;

            if (0 <= dPitch && dPitch <= 90) {
                DJIGimbal.DJIGimbalAngleRotation
                        pitch = new DJIGimbal.DJIGimbalAngleRotation(true, dPitch, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                DJIGimbal.DJIGimbalAngleRotation
                        roll = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                DJIGimbal.DJIGimbalAngleRotation
                        yaw = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.CounterClockwise);
                str.append("Sent -> gimbal pitch=" + dPitch);
                mActivity.mGimbal.rotateGimbalByAngle(DJIGimbal.DJIGimbalRotateAngleMode.AbsoluteAngle, pitch, roll, yaw, new DJIBaseComponent.DJICompletionCallback(){
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError != null) {
                            logger.appendLog(djiError.getDescription());
                            str.append(", result=" + djiError.getDescription() + "\n");
                        }
                    }
                });

            }
        }

        if (mActivity.isVideoRecording()) {
            str.append("Recording=" + mActivity.getTimeString() + "\n");
        }

        mActivity.showText(str.toString());
    }

    public void registerListener() {
        mSensorManager.registerListener(this, accelerometer, android.hardware.SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, android.hardware.SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    private String calculateOrientation() {

        double azimut = Math.toDegrees(orientation[0]) ;
        String result = "";

        if(azimut >= -5 && azimut < 5){
            result = "正北";
        }
        else if(azimut >= 5 && azimut < 85){
            result = "东北";
        }
        else if(azimut >= 85 && azimut <=95){
            result = "正东";
        }
        else if(azimut >= 95 && azimut <175){
            result = "东南";
        }
        else if((azimut >= 175 && azimut <= 180) || (azimut) >= -180 && azimut < -175){
            result = "正南";
        }
        else if(azimut >= -175 && azimut <-95){
            result = "西南";
        }
        else if(azimut >= -95 && azimut < -85){
            result = "正西";
        }
        else if(azimut >= -85 && azimut <-5){
            result = "西北";
        }
        return result;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = android.hardware.SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                android.hardware.SensorManager.getOrientation(R, orientation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    final static int UP       = 0;
    final static int LEFT     = 1;
    final static int RIGHT    = 2;
    final static int DOWN     = 3;
    final static int CENTER   = 4;

    float xaxis;
    float yaxis;

    int directionPressed = -1; // initialized to -1

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        InputDevice input = event.getDevice();
        if(null == input) {
            return false;
        } else {
            String deviceName = getUniqueName(input);
            // Check that the event came from a game controller
            if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                    InputDevice.SOURCE_JOYSTICK &&
                    event.getAction() == MotionEvent.ACTION_MOVE) {

                int press = getDirectionPressed(event);

                // Process all historical movement samples in the batch
                final int historySize = event.getHistorySize();

                // Process the movements starting from the
                // earliest historical position in the batch
                for (int i = 0; i < historySize; i++) {
                    // Process the event at historical position i
                    processJoystickInput(event, i);
                }

                // Process the current movement sample in the batch (position -1)
                processJoystickInput(event, -1);
                return true;
            }

            return true;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        // Handle DPad keys and fire button on initial down but not on
        // auto-repeat.

        int press = getDirectionPressed(event);

        boolean handled = false;
        if (event.getRepeatCount() == 0) {
            switch (event.getKeyCode()) {
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
                case KeyEvent.KEYCODE_BUTTON_A:
                    mActuator.takeOff();
                    handled = true;
                    break;
                case KeyEvent.KEYCODE_BUTTON_B:
                    mActuator.autoLanding();
                    handled = true;
                    break;
                default:
                    handled = true;
                    break;
            }
        }
        return handled;
    }


    public int getDirectionPressed(InputEvent event) {
        if (!isDpadDevice(event)) {
            return -1;
        }

        // If the input event is a MotionEvent, check its hat axis values.
        if (event instanceof MotionEvent) {

            // Use the hat axis value to find the D-pad direction
            MotionEvent motionEvent = (MotionEvent) event;
            xaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_X);
            yaxis = motionEvent.getAxisValue(MotionEvent.AXIS_HAT_Y);

            // Check if the AXIS_HAT_X value is -1 or 1, and set the D-pad
            // LEFT and RIGHT direction accordingly.
            if (Float.compare(xaxis, -1.0f) == 0) {
                directionPressed =  LEFT;
            } else if (Float.compare(xaxis, 1.0f) == 0) {
                directionPressed =  RIGHT;
            }
            // Check if the AXIS_HAT_Y value is -1 or 1, and set the D-pad
            // UP and DOWN direction accordingly.
            else if (Float.compare(yaxis, -1.0f) == 0) {
                directionPressed =  UP;
            } else if (Float.compare(yaxis, 1.0f) == 0) {
                directionPressed =  DOWN;
            }
        }

        // If the input event is a KeyEvent, check its key code.
        else if (event instanceof KeyEvent) {

            // Use the key code to find the D-pad direction.
            KeyEvent keyEvent = (KeyEvent) event;
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                directionPressed = LEFT;
            } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                directionPressed = RIGHT;
            } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                directionPressed = UP;
            } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                directionPressed = DOWN;
            } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                directionPressed = CENTER;
            }
        }
        return directionPressed;
    }


    private static float getCenteredAxis(MotionEvent event,
                                         InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis):
                            event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

    private void processJoystickInput(MotionEvent event,
                                      int historyPos) {

        InputDevice mInputDevice = event.getDevice();

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float x = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_X, historyPos);
        if (x == 0) {
            x = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_HAT_X, historyPos);
        }
        if (x == 0) {
            x = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_Z, historyPos);
        }

        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float y = getCenteredAxis(event, mInputDevice,
                MotionEvent.AXIS_Y, historyPos);
        if (y == 0) {
            y = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_HAT_Y, historyPos);
        }
        if (y == 0) {
            y = getCenteredAxis(event, mInputDevice,
                    MotionEvent.AXIS_RZ, historyPos);
        }

        // Update based on the new x and y values
        this.xaxis = x;
        this.yaxis = y;
    }

    public static String getUniqueName(InputDevice device) {
        return device.getName() + "_" + device.getId();
    }

    public static boolean isDpadDevice(InputEvent event) {
        // Check that input comes from a device with directional pads.
        if ((event.getSource() & InputDevice.SOURCE_DPAD)
                != InputDevice.SOURCE_DPAD) {
            return true;
        } else {
            return false;
        }
    }
}
