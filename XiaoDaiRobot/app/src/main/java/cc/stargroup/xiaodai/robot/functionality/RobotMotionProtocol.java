package cc.stargroup.xiaodai.robot.functionality;

/**
 @brief Protocol that provides access to the iDevice's IMU.

 The protocol allows for starting and stopping the IMU, setting its update
 frequency, and accessings the data from the IMU as well as post-processed data
 that is based off IMU data.
 */
public interface RobotMotionProtocol {

    /**
     Set to YES to start RobotMotion module
     If not explicitly changed, runs at IMU_DEFAULT_UPDATE_FREQUENCY
     */
    boolean isRobotMotionEnabled();

    /**
     Indicates if RobotMotion module is ready to be used.  If this flag returns NO
     then all requests for data from RobotMotion will return zero.
     */
    boolean isRobotMotionReady();

    /**
     Set refresh rate of IMU (Hz).
     Defaults to IMU_DEFAULT_UPDATE_FREQUENCY
     Clamped between IMU_MIN_UPDATE_FREQUENCY and IMU_MAX_UPDATE_FREQUENCY
     */
    float robotMotionDataUpdateRate();

    /**
     @brief Sets the iDevice's current attitude as the reference attitude that is
     used to determine the attitude of the mobility platform.

     It is intended that this reference is taken when the robot is situated on level
     ground.  It should not be called before RobotMotionReady has been set to YES by
     RMCore (automatically done internally).

     @return Returns YES if RobotMotion was ready and reference attitude was able to
     be set.  If NO is returned platformAttitude should _not_ be used.
     */
    boolean takeDeviceReferenceAttitude();

    /**
     Yaw rate of the platform around the gravity vector.
     */
    float platformYawRate();

    /**
     Platform acceleration along the direction of the gravity vector.
     */
    Acceleration platformAcceleration();

    /**
     Attitude of the platform.  Accuracy is depenedent on the platform being on
     level ground when the reference was taken (via takeReferenceAttitude method).
     */
    CoreAttitude platformAttitude();

    /**
     iDevice's raw accelerometer data.
     */
    Acceleration deviceAccelerometer();

    /**
     iDevice's raw gyroscope data.
     */
    RotationRate deviceGyroscope();

    /**
     Acceleration of the iDevice (with gravity acceleration removed).
     */
    Acceleration deviceAcceleration();

    /**
     Direction of the gravity vector.
     */
    Acceleration deviceGravity();

    /**
     Angular rotation rate of the iDevice.
     */
    RotationRate deviceRotationRate();

    /**
     iDevice's orienatation in space.
     */
    Quaternion deviceAttitude();
}
