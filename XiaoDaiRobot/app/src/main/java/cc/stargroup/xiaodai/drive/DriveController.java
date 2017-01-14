package cc.stargroup.xiaodai.drive;

/**
 * Created by Foam on 2017/1/15.
 */

public class DriveController {

    private float leftWheelVal;
    private float rightWheelVal;
    private float targetLeftWheelVal;
    private float targetRightWheelVal;

    public float leftWheelVal() {
        return leftWheelVal;
    }

    public float rightWheelVal() {
        return rightWheelVal;
    }

    public float targetLeftWheelVal() {
        return targetLeftWheelVal;
    }

    public float targetRightWheelVal() {
        return targetRightWheelVal;
    }

    public void setLeftWheelVal(float leftWheelVal) {
        this.leftWheelVal = leftWheelVal;
    }

    public void setRightWheelVal(float rightWheelVal) {
        this.rightWheelVal = rightWheelVal;
    }

    public void setTargetLeftWheelVal(float targetLeftWheelVal) {
        this.targetLeftWheelVal = targetLeftWheelVal;
    }

    public void setTargetRightWheelVal(float targetRightWheelVal) {
        this.targetRightWheelVal = targetRightWheelVal;
    }

    public void setSetpoint(float heading) {

    }

    public void setEnabled(boolean enabled) {

    }
}
