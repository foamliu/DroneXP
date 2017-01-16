package cc.stargroup.xiaodai.sensing;

/**
 * Created by Foam on 2017/1/16.
 */

public class Quaternion {
    private float x,y,z,w;

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public float w() {
        return w;
    }
}

