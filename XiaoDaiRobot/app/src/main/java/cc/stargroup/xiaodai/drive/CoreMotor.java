package cc.stargroup.xiaodai.drive;

import cc.stargroup.xiaodai.MainActivity;
import cc.stargroup.xiaodai.robot.CoreRobot;
import cc.stargroup.xiaodai.utilities.Util;

/**
 @brief An CoreMotor object provides an interface to the internal state and
 configuration of a motor.

 This interface is exclusively read-only properties that peek into a particular
 motor's state, including:
 - CoreMotor axis
 - PWM scalar
 - Power level
 - Current draw
 */
public class CoreMotor {
    private static final String TAG = CoreMotor.class.getSimpleName();
    private boolean isOverCurrent = false;

    private CoreRobot robot;

    /**
     The power level of the motor (in the range [-1.0, 1.0])
     */
    private float powerLevel;

    /**
     The PWM scalar of the motor (the maximum native integer value to send to
     the motor)

     For ROMO3A, this is 255
     */
    private int pwmScalar;

    public float powerLevel() {
        return powerLevel;
    }

    public int pwmScalar() {
        return pwmScalar;
    }

    public void setPowerLevel(float powerLevel) {
        // ensure that motor doesn't get power if it's been drawing too much current
        if(this.isOverCurrent)
        {
            powerLevel = 0;
        }

        powerLevel = Util.clamp(-1.0f, powerLevel, 1.0f);
        this.powerLevel = powerLevel;

        int pwmCommand = (int)(this.pwmScalar * powerLevel);

        //this.robot.communication setMotorNumber:_motorAxis commandType:RMMotorCommandTypePWM value:pwmCommand];
    }

    public void setPwmScalar(int pwmScalar) {
        this.pwmScalar = pwmScalar;
    }

    public CoreMotor() {

    }
}
