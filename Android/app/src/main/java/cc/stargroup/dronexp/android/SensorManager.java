package cc.stargroup.dronexp.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.List;

import dji.sdk.Battery.DJIBattery;
import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by Foam on 2016/9/25.
 */
public class SensorManager implements SensorEventListener {
    Logger logger = new Logger();
    MainActivity mActivity;
    ActuatorManager mActuator;
    android.hardware.SensorManager mSensorManager;

    //==============手机===============
    Sensor accelerometer;
    Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;
    float orientation[] = new float[3];

    float mXAxis = 0F; // 向左-1，向右+1
    float mYAxis = 0F; // 向上-1，向下+1

    boolean isUpPressed = false;
    boolean isDownPressed = false;

    public SensorManager(MainActivity activity, ActuatorManager actuator) {
        this.mActivity = activity;
        this.mActuator = actuator;

        mSensorManager = (android.hardware.SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void Sense() {

        DJIBattery battery = mActivity.mBattery;

        final StringBuilder str = new StringBuilder();
        str.append(String.format("Head -> pitch=%f roll=%f yaw=%f", getHeadPitch(), getHeadRoll(), getHeadYaw()));
        str.append(" ").append(calculateOrientation(getHeadYaw()));
        str.append("Joystick X-Axis: ").append(mXAxis).append(", Y-Axis").append(mYAxis).append("\n");

        if (mActivity.isControlled()) {
            mActuator.sendData(getHeadPitch(), getHeadRoll(), getHeadYaw(), mXAxis, mYAxis, isUpPressed, isDownPressed);
        }

        if (mActivity.mFlightController != null) {
            DJIFlightControllerDataType.DJIAttitude attitude = mActivity.mFlightController.getCurrentState().getAttitude();
            String yaw = String.format("%.2f", attitude.yaw);
            String pitch = String.format("%.2f", attitude.pitch);
            String roll = String.format("%.2f", attitude.roll);

//            DJIFlightControllerDataType.DJILocationCoordinate3D location = mActivity.mFlightController.getCurrentState().getAircraftLocation();
//            String altitude = String.format("%.2f", location.getAltitude());
//            String latitude = String.format("%.2f", location.getLatitude());
//            String longitude = String.format("%.2f", location.getLongitude());

            str.append("FlightController -> pitch : ").append(pitch).append(",  roll: ").append(roll).append(", yaw: ").append(yaw).append("\n");
        }

        if (mActivity.mGimbal != null) {
            DJIGimbal.DJIGimbalAttitude attitude = mActivity.mGimbal.getAttitudeInDegrees();
            str.append("Gimbal -> pitch : ").append(attitude.pitch).append(", roll : ").append(attitude.roll).append(", yaw : ").append(attitude.yaw).append("\n");
        }

        if (mActivity.isVideoRecording()) {
            str.append("Recording=").append(mActivity.getTimeString()).append("\n");
        }

        if (battery != null && battery.isConnected()) {
            final int numOFCell = battery.getNumberOfCell();
            str.append(numOFCell + " cells, ");
            battery.getCellVoltages(new DJIBaseComponent.DJICompletionCallbackWith<List<DJIBattery.DJIBatteryCell>>() {
                @Override
                public void onSuccess(List<DJIBattery.DJIBatteryCell> djiBatteryCells) {
                    for (int i=0;i<numOFCell;i++) {
                        DJIBattery.DJIBatteryCell cell =  djiBatteryCells.get(i);
                        str.append(cell.getVoltage() + " V, ");
                    }
                }

                @Override
                public void onFailure(DJIError djiError) {
                    final StringBuilder message = new StringBuilder("getCellVoltages: " + djiError.getDescription());
                    logger.appendLog(message.toString());
                }
            });
        }

        mActivity.showText(str.toString());
    }

    public float getHeadPitch() {
        double mobileRoll = Math.toDegrees(orientation[2]);
        float headPitch = (float) mobileRoll + 90;
        if (headPitch >= 180) {
            headPitch = (float) mobileRoll - 270;
        }

        return Math.round(headPitch);
    }

    public float getHeadRoll() {
        double mobilePitch = Math.toDegrees(orientation[1]);
        float headRoll = -(float) mobilePitch;
        return Math.round(headRoll);
    }

    public float getHeadYaw() {
        double mobileYaw = Math.toDegrees(orientation[0]);

        float headYaw = (float) mobileYaw + 90;
        if (headYaw >= 180) {
            headYaw = (float) mobileYaw - 270;
        }
        return Math.round(headYaw / 2) * 2;
    }

    public void registerListener() {
        mSensorManager.registerListener(this, accelerometer, android.hardware.SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, android.hardware.SensorManager.SENSOR_DELAY_UI);
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    private String calculateOrientation(float yaw) {
        String result = "";

        if (yaw >= -5 && yaw < 5) {
            result = "正北";
        } else if (yaw >= 5 && yaw < 85) {
            result = "东北";
        } else if (yaw >= 85 && yaw <= 95) {
            result = "正东";
        } else if (yaw >= 95 && yaw < 175) {
            result = "东南";
        } else if ((yaw >= 175 && yaw <= 180) || (yaw) >= -180 && yaw < -175) {
            result = "正南";
        } else if (yaw >= -175 && yaw < -95) {
            result = "西南";
        } else if (yaw >= -95 && yaw < -85) {
            result = "正西";
        } else if (yaw >= -85 && yaw < -5) {
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

    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        InputDevice input = event.getDevice();
        if (null == input) {
            return false;
        } else {
            String deviceName = getUniqueName(input);
            //logger.appendLog(deviceName);
            // Check that the event came from a game controller
            if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                    InputDevice.SOURCE_JOYSTICK &&
                    event.getAction() == MotionEvent.ACTION_MOVE) {

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
        boolean handled = false;

        if (event.getRepeatCount() == 0) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BUTTON_A:
                        isUpPressed = true;
                        handled = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:
                        isDownPressed = true;
                        handled = true;
                        break;
                    default:
                        handled = true;
                        break;
                }
            }
            else if (event.getAction() == KeyEvent.ACTION_UP) {
                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_BUTTON_A:
                        isUpPressed = false;
                        handled = true;
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:
                        isDownPressed = false;
                        handled = true;
                        break;
                    default:
                        handled = true;
                        break;
                }
            }

        }
        return handled;
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
                    historyPos < 0 ? event.getAxisValue(axis) :
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
        this.mXAxis = x;
        this.mYAxis = y;
    }

    public static String getUniqueName(InputDevice device) {
        return device.getName() + "_" + device.getId();
    }

}
