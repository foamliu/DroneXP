package cc.stargroup.xiaodai.sensing;

import android.content.Context;

/**
 @brief An DeviceMotion object stores data from the iDevice's IMU.

 The data in this module pertains directly to the iDevice (and not necessarily
 the mobility platform, or, robot as a whole).
 */
public class DeviceMotion {

    private MotionInterface motionInterface;
    private EnvironmentInterface environmentInterface;
    private IMUData freshIMUData;

    public DeviceMotion(Context context) {
        motionInterface = new MotionInterface(context);
        environmentInterface = new EnvironmentInterface(context);
        freshIMUData = new IMUData();
    }

    // raw IMU sensor data
    public Acceleration accelerometer() {
        return freshIMUData.accelerometer();
    }

    public RotationRate gyroscope() {
        return freshIMUData.gyroscope();
    }

    // conditioned IMU data (see RobotMotion Protocol for explanation of each)
    public Acceleration deviceAcceleration() {
        return freshIMUData.deviceAcceleration();
    }

    public Acceleration gravity() {
        return freshIMUData.gravity();
    }

    public Quaternion rotationRate() {
        return freshIMUData.rotationRate();
    }

    public Attitude attitude() {
        return freshIMUData.attitude();
    }

    // the latest packet of IMU data
    public IMUData freshIMUData() {
        return null;
    }


    public void registerListener() {
        motionInterface.registerListener();
        environmentInterface.registerListener();
    }

    public void unregisterListener() {
        motionInterface.unregisterListener();
        environmentInterface.unregisterListener();
    }
}
