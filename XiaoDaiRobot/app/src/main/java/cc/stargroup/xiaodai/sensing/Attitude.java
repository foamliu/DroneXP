package cc.stargroup.xiaodai.sensing;

/**
 @struct Attitude
 @brief This class is used for storing a (roll, pitch, yaw) attitude triplet.
 */
public class Attitude {
    /**
     Rotation about axis parallel to direction of forward motion (degrees).
     */
    private float roll;
    /**
     Rotation about the axis normal to the direction of forward motion (degrees).
     */
    private float pitch;

    /**
     Rotation about the axis normal to the ground (degrees).
     */
    private float yaw;

    public Attitude(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public float roll() {
        return roll;
    }

    public float pitch() {
        return pitch;
    }

    public float yaw() {
        return yaw;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
