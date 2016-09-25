package cc.stargroup.dronexp.android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import dji.sdk.FlightController.DJIFlightControllerDataType;
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

        String str = String.format("Phone: azimut=%f pitch=%f roll=%f", Math.toDegrees(orientation[0]), Math.toDegrees(orientation[1]), Math.toDegrees(orientation[2]));
        str = str + " " + calculateOrientation();
        logger.appendLog(str);
        Log.i(TAG, str);

        if (mActivity.mFlightController != null) {
            DJIFlightControllerDataType.DJIAttitude attitude = mActivity.mFlightController.getCurrentState().getAttitude();
            String yaw = String.format("%.2f", attitude.yaw);
            String pitch = String.format("%.2f", attitude.pitch);
            String roll = String.format("%.2f", attitude.roll);

            DJIFlightControllerDataType.DJILocationCoordinate3D location = mActivity.mFlightController.getCurrentState().getAircraftLocation();
            String altitude = String.format("%.2f", location.getAltitude());
            String latitude = String.format("%.2f", location.getLatitude());
            String longitude = String.format("%.2f", location.getLongitude());

            str = "Yaw : " + yaw + ", Pitch : " + pitch + ", Roll : " + roll + "\n" + ", Altitude : " + altitude +
                    ", Latitude : " + latitude +
                    ", Longitude : " + longitude;
            logger.appendLog(str);
        }

        if (mActivity.mRemoteController != null) {
            mActivity.mRemoteController.getRCWheelControlGimbalSpeed(new DJIBaseComponent.DJICompletionCallbackWith<Short>() {
                @Override
                public void onSuccess(Short aShort) {
                    String str = "GimbalDirection: " + aShort;
                    logger.appendLog(str);
                }

                @Override
                public void onFailure(DJIError djiError) {
                    logger.appendLog(djiError.getDescription());
                }
            });
        }

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
