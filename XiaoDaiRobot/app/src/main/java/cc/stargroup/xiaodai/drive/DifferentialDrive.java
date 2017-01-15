package cc.stargroup.xiaodai.drive;

import android.util.Log;

import cc.stargroup.xiaodai.MainActivity;
import cc.stargroup.xiaodai.robot.CoreRobot;

/**
 * Provides the most fundamental high-level commands
 // needed to control the mobility of a differential drive robot.
 */
public class DifferentialDrive {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CoreRobot robot;

    private DriveController headingController;
    private DriveController radiusController;

    private CoreMotor leftDriveMotor;
    private CoreMotor rightDriveMotor;

    private boolean driving = false;

    /**
     * Directly applies voltage to wheel motors propotional to input power level
     * provided.
     *  Values are on the interval [-1.,1.] (-1.= power backwards).
     */
    public void driveWithPower(float leftMotorPower, float rightMotorPower) {
        // make sure closed-loop controllers stop trying to control motors
        this.disableClosedLoopControllers();

        // issue motor command
        leftDriveMotor.setPowerLevel(leftMotorPower);
        rightDriveMotor.setPowerLevel(rightMotorPower);

        if (leftMotorPower != 0 || rightMotorPower != 0) {
            this.driving = true;
        } else {
            this.driving = false;
        }
    }

    /**
     * Commands robot to drive with symmetric input power while attempting to
     * to maintain initial heading.
     *  Power value on the interval [-1., 1.]
     */
    public void driveWithPower(float power) {

    }

    // drive on given heading at input power
    public void driveWithHeading(float heading, float power)
    {
        float kLatencyFudgeFactor = 2.5f;

        // make sure nothing else tries to control motors
        this.disableClosedLoopControllers();

        // issue motor command immediately
        leftDriveMotor.setPowerLevel(power);
        rightDriveMotor.setPowerLevel(power);

        // setup for closed-loop control
        if (power != 0)
        {
            this.driving = true;

            // set the desired target driving speed in PWM "units"
            headingController.setTargetLeftWheelVal((power * leftDriveMotor.pwmScalar()));
            headingController.setTargetRightWheelVal(headingController.targetLeftWheelVal());

            // set current driving speed to match
            headingController.setLeftWheelVal(headingController.targetLeftWheelVal());
            headingController.setRightWheelVal(headingController.targetRightWheelVal());

            // estimate where robot will be pointed when the setpoint is taken (this
            // is necessary because the IMU's sampling rate is far too slow when
            // compared to Romo's turn-rate capability)
            heading += (robot.platformYawRate() * (1/20.) * kLatencyFudgeFactor);
            // 20 Hz is the assumed update rate of the sensors.  I am leaving this
            // hard-coded as a reminder that it needs to be changed once the IMU
            // system is moved to a free-running format with a known frequency

            // keep results on [-180, 180] range (need to put this in
            // RMCoreCircleMath.h)
            if (heading > 180) {heading -= 360;}
            else if (heading < -180) {heading += 360;}

            // set heading & enable controller
            headingController.setSetpoint(heading);
            headingController.setEnabled(true);
        }
        else
        {
            this.driving = true;
        }

        // alert user if command issued without IMU being active
        if (!this.robot.isRobotMotionEnabled() && power != 0)
        {
            String warningString = "WARNING: 'Drive With Heading' command called with RobotMotion disabled; drive output is unpredictable";

            Log.w(TAG, warningString);
        }
    }

    private void disableClosedLoopControllers() {

    }
}
