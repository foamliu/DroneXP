package cc.stargroup.xiaodai.sensing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import cc.stargroup.xiaodai.MainActivity;

/**
 * Created by Foam on 2017/1/16.
 */

public class EnvironmentInterface implements SensorEventListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SensorManager sensorManager;

    private float temperature;  // Ambient air temperature.(°C)
    private float light;        // Illuminance.(lx)
    private float pressure;     // Ambient air pressure.(hPa or mbar)
    private float humidity;     // Ambient relative humidity.(%)

    public EnvironmentInterface(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void registerListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), android.hardware.SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), android.hardware.SensorManager.SENSOR_DELAY_UI);

    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            temperature = event.values[0];
            Log.d(TAG, String.format("temperature: %f", temperature));
        }
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            light = event.values[0];
            Log.d(TAG, String.format("light: %f", light));
        }
        if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
            pressure = event.values[0];
            Log.d(TAG, String.format("pressure: %f", pressure));
        }
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidity = event.values[0];
            Log.d(TAG, String.format("humidity: %f", humidity));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
