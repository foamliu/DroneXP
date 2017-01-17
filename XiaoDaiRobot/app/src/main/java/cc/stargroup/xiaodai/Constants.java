package cc.stargroup.xiaodai;

/**
 * Created by Foam on 2017/1/16.
 */

public class Constants {
    /**
     The default frequency that the IMU will update, Hz
     */
    public final static int IMU_DEFAULT_UPDATE_FREQUENCY = 20;

    /**
     The maximum frequency that the IMU will update, Hz
     Trying to set -robotMotionDataUpdateRate to higher frequencies will be capped here
     */
    public final static int IMU_MAX_UPDATE_FREQUENCY = 60;

    /**
     The minumum frequency that the IMU will update, Hz
     Trying to set -robotMotionDataUpdateRate to lower frequencies will be capped here
     */
    public final static int IMU_MIN_UPDATE_FREQUENCY = 1;

    public final static int DEFAULT_GLOBAL_IMU_FREQUENCY = 20; // (Hz) Default IMU sampling frequency

    /** Macro defining the radius of infinity */
    public final static float DRIVE_RADIUS_STRAIGHT = 9999f;

    /** Macro for defining the radius to turn in place */
    public final static int DRIVE_RADIUS_TURN_IN_PLACE = 0;

    /** Macro that defines a speed value indicating the robot cannot determine its
     speed */
    public final static float DRIVE_SPEED_UNKNOWN = 9999;

    /** Macro that defines the maximum and minimum robot headings (degrees) */
    public final static float MAX_HEADING = 180f;
    public final static float MIN_HEADING = -180f;

    public final static float kSendRate = 0.05f;              // 20Hz send rate for all commands
}
