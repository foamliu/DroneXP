package cc.stargroup.xiaodai.drive;

/**
 * Provides the most fundamental high-level commands
 // needed to control the mobility of a differential drive robot.
 */
public class DifferentialDrive {

    /**
     * Directly applies voltage to wheel motors propotional to input power level
     * provided.
     *  Values are on the interval [-1.,1.] (-1.= power backwards).
     */
    public void driveWithPower(float leftMotorPower, float rightMotorPower) {

    }

    /**
     * Commands robot to drive with symmetric input power while attempting to
     * to maintain initial heading.
     *  Power value on the interval [-1., 1.]
     */
    public void driveWithPower(float power) {

    }
}
