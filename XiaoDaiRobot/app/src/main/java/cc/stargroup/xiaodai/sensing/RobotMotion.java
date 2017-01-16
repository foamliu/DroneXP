package cc.stargroup.xiaodai.sensing;

import android.content.Context;

/** @file RobotMotion.h
 @brief Private header for Robot Motion module.  This is the central point for
 receiving and distrubuting IMU data.

 In addition to collecting IMU data this module is also used to do basic set up
 and enabling/disabling of the IMU unit (indirectly, through calls to a
 RMCoreRobot).  It acts as the handler for receiving IMU data and passes that
 data along to the iDevice and mobility platform modules where it may be
 further processed, as necessary.
 */
public class RobotMotion {

    private DeviceMotion iDevice;

    public RobotMotion(Context context) {
        iDevice = new DeviceMotion(context);
    }

    /**
     This is the place where IMU data from the iDevice's framework is sent.  Some
     post-processing may occur there.
     */
    public DeviceMotion iDevice() {
        return iDevice;
    }

    /**
     Set to YES to enable RobotMotion (IMU).
     */
    public boolean isEnabled() {
        return true;
    }

    /**
     Indicates if RobotMotion is ready (IMU is sourcing good data)
     */
    public boolean ready() {
        return true;
    }

    /**
     Set refresh rate of the RobotMotion module (Hz)
     */
    public float dataUpdateRate;

    public void registerListener() {
        iDevice.registerListener();
    }

    public void unregisterListener() {
        iDevice.unregisterListener();
    }
}
