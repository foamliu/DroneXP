package cc.stargroup.xiaodai.drive;

import cc.stargroup.xiaodai.MainActivity;

/**
 @brief An CoreRobot object is a generic representation of a robot. It
 contains the robot's vitals and properties about the robot (whether it's
 connected, what type of commands it will respond to). A specific robot
 implementation will be a subclass of CoreRobot (e.g., RMCoreRobotRomo3).
 */
public class CoreRobot {
    private static final String TAG = MainActivity.class.getSimpleName();

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

    public float platformYawRate() {
        return 0.0f;
    }
}
