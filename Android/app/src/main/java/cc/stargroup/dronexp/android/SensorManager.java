package cc.stargroup.dronexp.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.RemoteController.DJIRemoteController;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by Foam on 2016/9/25.
 */
public class SensorManager implements SensorEventListener {
    private static final String TAG = MainActivity.class.getName();
    private Logger logger = new Logger();
    private MainActivity mActivity;
    private android.hardware.SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    float[] mGravity;
    float[] mGeomagnetic;
    float orientation[] = new float[3];

    public SensorManager(MainActivity activity) {
        this.mActivity = activity;

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
}
