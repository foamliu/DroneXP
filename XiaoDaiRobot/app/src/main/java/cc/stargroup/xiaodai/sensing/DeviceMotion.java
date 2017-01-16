package cc.stargroup.xiaodai.sensing;

/**
 @brief An DeviceMotion object stores data from the iDevice's IMU.

 The data in this module pertains directly to the iDevice (and not necessarily
 the mobility platform, or, robot as a whole).
 */
public class DeviceMotion {

    private IMUData freshIMUData;

    public DeviceMotion() {
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
}
