package cc.stargroup.xiaodai.sensing;

/**
 * Created by Foam on 2017/1/16.
 */

public class IMUData {

    private Acceleration accelerometer;
    private RotationRate gyroscope;
    private Acceleration deviceAcceleration;
    private Acceleration gravity;
    private Quaternion rotationRate;
    private Attitude attitude;

    public IMUData() {

    }

    public Acceleration accelerometer() {
        return accelerometer;
    }

    public RotationRate gyroscope() {
        return gyroscope;
    }

    public Acceleration deviceAcceleration() {
        return deviceAcceleration;
    }

    public Acceleration gravity() {
        return gravity;
    }

    public Quaternion rotationRate() {
        return rotationRate;
    }

    public Attitude attitude() {
        return attitude;
    }

    public void setAccelerometer(Acceleration accelerometer) {
        this.accelerometer = accelerometer;
    }

    public void setGyroscope(RotationRate gyroscope) {
        this.gyroscope = gyroscope;
    }

    public void setDeviceAcceleration(Acceleration deviceAcceleration) {
        this.deviceAcceleration = deviceAcceleration;
    }

    public void setGravity(Acceleration gravity) {
        this.gravity = gravity;
    }

    public void setRotationRate(Quaternion rotationRate) {
        this.rotationRate = rotationRate;
    }

    public void setAttitude(Attitude attitude) {
        this.attitude = attitude;
    }


}
