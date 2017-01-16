package cc.stargroup.xiaodai.sensing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
// This class is used to access the iDevice's IMU (Intertial Measurement Unit)
// through Apple's CoreMotion framework.  The IMU consists of a three-axis
// accelerometer, three-axis gyroscope. The raw data from each of these sensors may be accessed
// directly or a conditioned form of the data may be accessed.  The conditioned
// data is the result of sensor fusion and/or filtering that is performed by the
// CoreMotion framework.  For example, the device's attitude is provided by
// filtering gyroscope and accelerometer data together.
//
// Data can be accessed manually via a polling technique or at regular intervals
// through a callback system.  If using the callback system the IMUDataUpdate
// protocol must be adopted so that the user of the class can be alerted each
// time new data is available.  Each time new data is available the relavent
// delegate method is provided with a timestamp of when the data was
// sampled. The frequency at which the sensors are updated (or at least the
// frequency at which the callback block is alerted) can be adjusted between 1
// and 100 Hz (maybe faster in future hardware) and is controlled through public
// properties.  When new data is available it is accessed through the public get
// methods.  Public methods are also provided for testing the device to see
// which IMU sensors are available and when they are active (have been enabled
// and are ready to provide data).
 */
public class MotionInterface implements SensorEventListener {
    private SensorManager sensorManager;
    //private Sensor accelerometer;

    private float[] accelerometer = new float[3];   // Acceleration force along the x/y/z axis (including gravity, m/s2).
    private float[] geomagnetic = new float[3];
    private float[] gravity = new float[3];
    private float[] gyroscope = new float[3];
    private float[] acceleration = new float[3];    // Acceleration force along the x/y/z axis (excluding gravity, m/s2).
    private float[] orientation = new float[3];
    private float[] rotationRate = new float[4];

    private float mXAxis = 0F; // 向左-1，向右+1
    private float mYAxis = 0F; // 向上-1，向下+1

    public MotionInterface(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        registerListener();
    }

    public void registerListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), android.hardware.SensorManager.SENSOR_DELAY_UI);


    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometer = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            geomagnetic = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravity = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroscope = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            acceleration = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            rotationRate = event.values;
        }

        if (accelerometer != null && geomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = android.hardware.SensorManager.getRotationMatrix(R, I, accelerometer, geomagnetic);
            if (success) {
                android.hardware.SensorManager.getOrientation(R, orientation);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public IMUData getData() {
        IMUData data = new IMUData();

        data.setAccelerometer(new Acceleration(accelerometer[0], accelerometer[1], accelerometer[2]));
        data.setGravity(new Acceleration(gravity[0], gravity[1], gravity[2]));
        data.setGyroscope(new RotationRate(gyroscope[0], gyroscope[1], gyroscope[2]));
        data.setDeviceAcceleration(new Acceleration(acceleration[0], acceleration[1], acceleration[2]));
        data.setAttitude(new Attitude(orientation[0], orientation[1], orientation[2]));
        data.setRotationRate(new Quaternion(rotationRate[0], rotationRate[1], rotationRate[2], rotationRate[3]));

        return data;
    }
}
