package cc.stargroup.xiaodai.robot;

/**
 @brief The interface for querying the hardware about its internal state.

 This includes battery level, charging status, model name/number,
 serial number, firmware version, hardware revision, and
 manufacturer. Every CoreRobot instance has an associated
 CoreRobotVitals object.
 */
public class CoreRobotVitals {
    /**
     The robot's battery level
     Values are on the interval [0,1] (1 = fully charged)
     */
    public float batteryLevel() {
        return 1;
    }

    /**
     A boolean value representing the robot's charging state
     */
    public boolean isCharging() {
        return false;
    }
}
