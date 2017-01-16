package cc.stargroup.xiaodai.sensing;

/**
 * Created by Foam on 2017/1/16.
 */

public class Acceleration {

    private float forceX;   // Acceleration force along the x axis (m/s2).
    private float forceY;   // Acceleration force along the y axis (m/s2).
    private float forceZ;   // Acceleration force along the z axis (m/s2).

    public Acceleration(float forceX, float forceY, float forceZ) {
        this.forceX = forceX;
        this.forceY = forceY;
        this.forceZ = forceZ;
    }

    public float forceX() {
        return forceX;
    }

    public float forceY() {
        return forceY;
    }

    public float forceZ() {
        return forceZ;
    }

}
