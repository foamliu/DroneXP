package cc.stargroup.xiaodai.robot.functionality;

/**
 @struct CoreAttitude
 @brief This class is used for storing a (roll, pitch, yaw) attitude triplet.
 */
public class CoreAttitude {
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

    public CoreAttitude(float roll, float pitch, float yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
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
