package cc.stargroup.xiaodai.robot.functionality;

import cc.stargroup.xiaodai.drive.CoreMotor;

/**
 @brief A protocol implemented by CoreRobot instances that can support
 Differential Drive commands.

 This protocol provides methods for interacting with a Differential Drive
 robot. When an CoreRobot object is instantiated (and when it implements the
 DifferentialDriveProtocol), you may send the robot commands through this
 protocol.

 Note: A differential drive robot has movement that is based
 on two separately driven wheels placed on either side of the robot. For
 more information on differential drive robots,
 <a href="http://en.wikipedia.org/wiki/Differential_wheeled_robot">check
 out the wikipedia article.</a>
 */
public interface DifferentialDriveProtocol extends DriveProtocol {
    /**
     A read-only object representing the left RMCoreMotor

     Read this property to access properties of the motor (e.g., power level,
     current)
     */
     CoreMotor leftDriveMotor();

    /**
     A read-only object representing the right RMCoreMotor

     Read this property to access properties of the motor (e.g., power level,
     current)
     */
     CoreMotor rightDriveMotor();



    /**
     Commands robot to drive with symmetric input power while attempting to
     to maintain initial heading.

     @param power Power value on the interval [-1.0, 1.0] (where -1.0 indicates
     backwards movement and 1.0 indicates forward movement).
     */
     void driveWithPower(float power);

    /**
      Directly applies voltage to wheel motors propotional to the input power level
      provided.

      @param leftMotorPower A value on the interval [-1.0, 1.0] that drives the left
      motor (-1.0 indicates fully backwards and 1.0 indicates fully forwards).

      @param rightMotorPower A value on the interval [-1.0, 1.0] that drives the
      right motor (where -1.0 indicates fully backwards and 1.0 indicates fully
      forwards).
      */
     void driveWithLeftMotorPower(float leftMotorPower, float rightMotorPower);

}
