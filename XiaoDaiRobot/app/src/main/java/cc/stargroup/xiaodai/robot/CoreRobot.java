package cc.stargroup.xiaodai.robot;

import cc.stargroup.xiaodai.MainActivity;
import cc.stargroup.xiaodai.drive.CoreDriveCommand;
import cc.stargroup.xiaodai.drive.CoreMotor;
import cc.stargroup.xiaodai.drive.CoreTurnCompletion;
import cc.stargroup.xiaodai.drive.CoreTurnFinishingAction;
import cc.stargroup.xiaodai.drive.DifferentialDrive;
import cc.stargroup.xiaodai.robot.functionality.Acceleration;
import cc.stargroup.xiaodai.robot.functionality.Constants;
import cc.stargroup.xiaodai.robot.functionality.CoreAttitude;
import cc.stargroup.xiaodai.robot.functionality.DifferentialDriveProtocol;
import cc.stargroup.xiaodai.robot.functionality.Quaternion;
import cc.stargroup.xiaodai.robot.functionality.RobotMotionProtocol;
import cc.stargroup.xiaodai.robot.functionality.RotationRate;

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

    public CoreRobot() {
        this.leftDriveMotor = new CoreMotor();
        this.rightDriveMotor = new CoreMotor();

        this.drive = new DifferentialDrive();
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
        return false;
    }

    @Override
    public float robotMotionDataUpdateRate() {
        return 0;
    }

    @Override
    public boolean takeDeviceReferenceAttitude() {
        return false;
    }

    public float platformYawRate() {
        return 0.0f;
    }

    @Override
    public Acceleration platformAcceleration() {
        return null;
    }

    @Override
    public CoreAttitude platformAttitude() {
        return null;
    }

    @Override
    public Acceleration deviceAccelerometer() {
        return null;
    }

    @Override
    public RotationRate deviceGyroscope() {
        return null;
    }

    @Override
    public Acceleration deviceAcceleration() {
        return null;
    }

    @Override
    public Acceleration deviceGravity() {
        return null;
    }

    @Override
    public RotationRate deviceRotationRate() {
        return null;
    }

    @Override
    public Quaternion deviceAttitude() {
        return null;
    }

    @Override
    public CoreMotor leftDriveMotor() {
        return leftDriveMotor;
    }

    @Override
    public CoreMotor rightDriveMotor() {
        return rightDriveMotor;
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
    public void driveWithLeftMotorPower(float leftMotorPower, float rightMotorPower) {

    }

    @Override
    public boolean isDriving() {
        return false;
    }

    @Override
    public float speed() {
        return 0;
    }

    @Override
    public CoreDriveCommand driveCommand() {
        return driveCommand;
    }

    @Override
    public void driveForwardWithSpeed(float speed) {

    }

    @Override
    public void driveBackwardWithSpeed(float speed) {

    }

    @Override
    public void stopDriving() {

    }

    @Override
    public void driveWithRadius(float radius, float speed) {

    }

    @Override
    public void turnByAngle(float angle, float radius, CoreTurnCompletion completion) {

    }

    @Override
    public void turnByAngle(float angle, float radius, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {

    }

    @Override
    public void turnByAngle(float angle, float radius, float speed, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {

    }

    @Override
    public void turnToHeading(float targetHeading, float radius, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {

    }

    @Override
    public void turnToHeading(float targetHeading, float radius, float speed, boolean forceShortestTurn, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion) {

    }
}
