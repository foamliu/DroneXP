package cc.stargroup.xiaodai.robot;

import android.content.Context;

import cc.stargroup.xiaodai.MainActivity;
import cc.stargroup.xiaodai.drive.CoreDriveCommand;
import cc.stargroup.xiaodai.drive.CoreMotor;
import cc.stargroup.xiaodai.drive.CoreTurnCompletion;
import cc.stargroup.xiaodai.drive.CoreTurnFinishingAction;
import cc.stargroup.xiaodai.drive.DifferentialDrive;
import cc.stargroup.xiaodai.sensing.Acceleration;
import cc.stargroup.xiaodai.Constants;
import cc.stargroup.xiaodai.sensing.Attitude;
import cc.stargroup.xiaodai.robot.functionality.DifferentialDriveProtocol;
import cc.stargroup.xiaodai.robot.functionality.RobotMotionProtocol;
import cc.stargroup.xiaodai.sensing.Quaternion;
import cc.stargroup.xiaodai.sensing.RobotMotion;
import cc.stargroup.xiaodai.sensing.RotationRate;

/**
 @brief An CoreRobot object is a generic representation of a robot. It
 contains the robot's vitals and properties about the robot (whether it's
 connected, what type of commands it will respond to). A specific robot
 implementation will be a subclass of CoreRobot.
 */
public class CoreRobot implements DifferentialDriveProtocol, RobotMotionProtocol {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CoreMotor leftDriveMotor;
    private CoreMotor rightDriveMotor;
    private DifferentialDrive drive;
    private RobotMotion robotMotion;


    /**
     The vitals object containing the robot's internal state (e.g., battery level,
     charging state).
     */
    private CoreRobotVitals vitals;

    /**
     Read-only property indicating whether the RMCoreRobot is connected.
     */
    private boolean connected;

    /**
     Read-only property indicating whether the RMCoreRobot is drivable.
     */
    private boolean drivable;

    /**
     Read-only property indicating whether the RMCoreRobot has a head tilt.
     */
    private boolean headTiltable;

    /**
     Read-only property indicating whether the RMCoreRobot has LED capabilities.
     */
    private boolean  LEDEquipped;

    /**
     Read-only property indicating whether the RMCoreRobot has IMU capabilities.
     */
    private boolean  IMUEquipped;

    private CoreDriveCommand driveCommand;
    private float speed;

    public CoreRobot(Context context) {
        this.leftDriveMotor = new CoreMotor();
        this.rightDriveMotor = new CoreMotor();

        this.drive = new DifferentialDrive();
        this.robotMotion = new RobotMotion(context);
    }


    public boolean isDrivable()
    {
        return true;
    }

    public boolean isHeadTiltable()
    {
        return false;
    }

    public boolean isLEDEquipped()
    {
        return true;
    }

    public boolean isIMUEquipped()
    {
        return true;
    }

    public boolean isConnected()
    {
        return true;
    }

    public float powerLevel()
    {
        return 0.0f;
    }

    /**
     Immediately stops all motion of every motor.
     */
    public void stopAllMotion() {

    }

    public boolean supportsFirmwareUpdating()
    {
        return false;
    }

    public boolean supportsReset()
    {
        return false;
    }

    public void updateFirmware(String fileURL)
    {

    }

    public void stopUpdatingFirmware()
    {

    }

    public void softReset()
    {

    }

    public boolean isRobotMotionEnabled() {
        return true;
    }

    @Override
    public boolean isRobotMotionReady() {
        return true;
    }

    @Override
    public float robotMotionDataUpdateRate() {
        return 0;
    }

    @Override
    public boolean takeDeviceReferenceAttitude() {
        return true;
    }

    public float platformYawRate() {
        return 0.0f;
    }

    @Override
    public Acceleration platformAcceleration() {
        return null;
    }

    @Override
    public Attitude platformAttitude() {
        return null;
    }

    @Override
    public Acceleration deviceAccelerometer() {
        return this.robotMotion.iDevice().accelerometer();
    }

    @Override
    public RotationRate deviceGyroscope() {
        return this.robotMotion.iDevice().gyroscope();
    }

    @Override
    public Acceleration deviceAcceleration() {
        return this.robotMotion.iDevice().deviceAcceleration();
    }

    @Override
    public Acceleration deviceGravity() {
        return this.robotMotion.iDevice().gravity();
    }

    @Override
    public Quaternion deviceRotationRate() {
        return this.robotMotion.iDevice().rotationRate();
    }

    @Override
    public Attitude deviceAttitude() {
        return this.robotMotion.iDevice().attitude();
    }

    @Override
    public void registerListener() {
        robotMotion.registerListener();
    }

    @Override
    public void unregisterListener() {
        robotMotion.unregisterListener();
    }

    // DriveProtocol Methods

    @Override
    public CoreMotor leftDriveMotor() {
        return leftDriveMotor;
    }

    @Override
    public CoreMotor rightDriveMotor() {
        return rightDriveMotor;
    }

    @Override
    public boolean isDriving() {
        return this.drive.isDriving() || speed == Constants.DRIVE_SPEED_UNKNOWN ;
    }

    @Override
    public float speed() {
        return this.speed;
    }

    @Override
    public CoreDriveCommand driveCommand() {
        return driveCommand;
    }

    @Override
    public void driveWithPower(float power) {
        if (power != 0) {
            this.speed = Constants.DRIVE_SPEED_UNKNOWN;
        } else {
            this.speed = 0.0f;
        }

        driveCommand = CoreDriveCommand.WithPower;

        this.drive.driveWithPower(power);
    }

    @Override
    public void driveWithMotorPower(float leftMotorPower, float rightMotorPower) {
        if (leftMotorPower != 0 || rightMotorPower != 0) {
            speed = Constants.DRIVE_SPEED_UNKNOWN;
        } else {
            speed = 0.0f;
        }
        this.drive.driveWithMotorPower(leftMotorPower, rightMotorPower);

        driveCommand = CoreDriveCommand.WithPower;
    }

    @Override
    public void driveForwardWithSpeed(float speed) {
        this.driveWithRadius(Constants.DRIVE_RADIUS_STRAIGHT, speed);

        driveCommand = CoreDriveCommand.Forward;
    }

    @Override
    public void driveBackwardWithSpeed(float speed) {
        this.driveForwardWithSpeed(-speed);

        driveCommand = CoreDriveCommand.Backward;
    }

    @Override
    public void driveWithRadius(float radius, float speed) {
        driveCommand = CoreDriveCommand.WithRadius;
        this.speed = speed;

        this.drive.driveWithRadius(radius, speed);
    }

    @Override
    public void driveWithHeading(float heading, float power) {
        if (power != 0) {
            this.speed = Constants.DRIVE_SPEED_UNKNOWN;
        } else {
            this.speed = 0.0f;
        }

        driveCommand = CoreDriveCommand.WithHeading;

        this.drive.driveWithHeading(heading, power);
    }

    @Override
    public void turnByAngle(float angle, float radius, CoreTurnCompletion completion) {
        this.drive.turnByAngle(angle,
                                radius,
                                CoreTurnFinishingAction.StopDriving,
                                completion);

        driveCommand = CoreDriveCommand.Turn;
    }

    @Override
    public void turnByAngle(float angle, float radius, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {
                                this.drive.turnByAngle(angle,
                                radius,
                                finishingAction,
                                completion);

        driveCommand = CoreDriveCommand.Turn;
    }

    @Override
    public void turnByAngle(float angle, float radius, float speed, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {
        this.drive.turnByAngle(angle,
                                radius,
                                speed,
                                finishingAction,
                                completion);

        driveCommand = CoreDriveCommand.Turn;
    }

    @Override
    public void turnToHeading(float targetHeading, float radius, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {
        this.drive.turnToHeading(targetHeading,
                                radius,
                                finishingAction,
                                completion);

        driveCommand = CoreDriveCommand.Turn;
    }

    @Override
    public void turnToHeading(float targetHeading, float radius, float speed, boolean forceShortestTurn, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {
        this.drive.turnToHeading(targetHeading,
                                radius,
                                speed,
                                forceShortestTurn,
                                finishingAction,
                                completion);

        driveCommand = CoreDriveCommand.Turn;
    }

    @Override
    public void stopDriving() {
        // this sets the motor powers to zero and disables any tracks PID
        // controllers that may be running.
        this.driveWithHeading(0, 0);

        driveCommand = CoreDriveCommand.Stop;
    }
}
