package cc.stargroup.xiaodai.sensing;

/**
 * Created by Foam on 2017/1/16.
 */

public class RotationRate {

    private float rateX;   // Rate of rotation around the x axis. (rad/s).
    private float rateY;   // Rate of rotation around the y axis. (rad/s).
    private float rateZ;   // Rate of rotation around the z axis. (rad/s).

    public RotationRate(float rateX, float rateY, float rateZ) {
        this.rateX = rateX;
        this.rateY = rateY;
        this.rateZ = rateZ;
    }

    public float rateX() {
        return rateX;
    }

    public float rateY() {
        return rateY;
    }

    public float rateZ() {
        return rateZ;
    }
}
