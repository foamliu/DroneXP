package cc.stargroup.xiaodai.robot.functionality;

import cc.stargroup.xiaodai.drive.CoreDriveCommand;
import cc.stargroup.xiaodai.drive.CoreTurnCompletion;
import cc.stargroup.xiaodai.drive.CoreTurnFinishing;
import cc.stargroup.xiaodai.drive.CoreTurnFinishingAction;

/**
 @brief The protocol for directly interfacing with the high-level motions
 of a driveable robot.

 When an CoreRobot object is instantiated (and when it implements
 DriveProtocol), you may send the robot drive commands and query its state
 based on the methods contained in this protocol.
 */
public interface DriveProtocol {
    /**
     Read-only property that indicates whether or not the robot is currently
     driving.
     */
    boolean isDriving();

    /**
     Read-only property that indicates the speed that the robot is instructed
     to drive at. If the speed is unknown, RM_DRIVE_SPEED_UNKNOWN is returned.
     (An example is when low-level commands are issued that don't necessarily
     correspond to a high-level speed)
     */
    float speed();

    /**
     Read-only property that indicates what the most recent drive command issued
     is.
     */
    CoreDriveCommand driveCommand();



    /**
     Commands robot to drive forward with input speed (meters / second) while
     attempting to maintain initial heading.

     @param speed Speed value (in m/s) on the interval [0, inf]
     */
    void driveForwardWithSpeed(float speed);

    /**
     Commands robot to drive backwards with input speed (m/s)while attempting to
     to maintain initial heading.

     @param speed Speed value (in m/s) on the interval [0, inf]
     */
    void driveBackwardWithSpeed(float speed);


    /**
     Immediately commands the robot to stop driving
     */
    void stopDriving();


    /**
     Commands robot to drive with the input speed (m/s) on an arc of the input
     radius (m).

     Note: Due to hardware limitations the robot's actual speed and radius
     will vary.

     @param radius The radius at which the robot should turn, on the interval
     [-Inf, Inf]
     - RM_DRIVE_RADIUS_TURN_IN_PLACE causes robot to turn-in-place.
     - RM_DRIVE_RADIUS_STRAIGHT causes robot to drive straight and actively
     hold its heading.
     - Positive radii cause robot to arc counter-clocwise when robot is
     driving forwards.

     @param speed How fast the robot should move in meters / second. Negative
     speeds cause the robot to drive backwards.
     */
    void driveWithRadius(float radius, float speed);


    /**
     Commands robot to drive until it rotates through the given angle.  Robot will
     drive with the specified radius.  The optimal speed and direction is
     automatically calculated such that the turn completes as fast as possible with
     minimal risk of overshooting the target heading. Stops the robot when finished.

     @param angle The angle (in degrees) that is to be turned through [-180, 180]
     - Positive angles correspond to the robot turning counter-clocwise.

     @param radius The radius at which the robot should drive, on the interval
     ~[-.75, .75]
     - RM_DRIVE_RADIUS_TURN_IN_PLACE causes robot to turn-in-place.

     @param completion Optional callback when target heading is reached or turn is
     aborted.
     */

    void turnByAngle(float angle, float radius, CoreTurnCompletion completion);


    /**
     Commands robot to drive until it rotates through the given angle.  Robot will
     drive with the specified radius.  The optimal speed and direction is
     automatically calculated such that the turn completes as fast as possible with
     minimal risk of overshooting the target heading.

     @param angle The angle (in degrees) that is to be turned through [-180, 180]
     - Positive angles correspond to the robot turning counter-clocwise.

     @param radius The radius at which the robot should drive, on the interval
     ~[-.75, .75]
     - RM_DRIVE_RADIUS_TURN_IN_PLACE causes robot to turn-in-place.

     @param finishingAction Dictates what should happen immediately after the target
     heading is reached (e.g. continue driving straight on target heading)

     @param completion Optional callback when target heading is reached or turn is
     aborted.
     */

    void turnByAngle(float angle, float radius, CoreTurnFinishingAction finishingAction, CoreTurnCompletion completion);


    /**
     Commands robot to drive until it rotates through the given angle.  Robot will
     drive with the specified radius and speed.  The direction is dictated by the
     sign of the angle (where positive angles cause counter-clockwise rotation).

     @param angle The angle (in degrees) that is to be turned through [-180, 180]
     - Positive angles correspond to the robot turning counter-clocwise.

     @param radius The radius at which the robot should drive, on the interval
     ~[-.75, .75]
     - RM_DRIVE_RADIUS_TURN_IN_PLACE causes robot to turn-in-place.
     - Positive radii cause robot to arc counter-clocwise when robot is driving
     forwards

     @param speed How fast the robot should move in meters / second. Negative
     speeds cause the robot to drive backwards.

     @param finishingAction Dictates what should happen immediately after the target
     heading is reached (e.g. continue driving straight on target heading)

     @param completion Optional callback when target heading is reached or turn is
     aborted.
     */
    void turnByAngle(float angle,
                     float radius,
                     float speed,
                     CoreTurnFinishingAction finishingAction,
                     CoreTurnCompletion completion);

    /**
     Commands robot to drive until it reaches the target heading.  Robot will drive
     with the specified radius.  The optimal speed and direction is automatically
     calculated such that the turn completes as fast as possible with minimal risk
     of overshooting the target heading.

     @param targetHeading The absolute heading (in degrees) that is the target
     [-180, 180]

     @param radius The radius at which the robot should drive, on the interval
     ~[-.75, .75]
     - RM_DRIVE_RADIUS_TURN_IN_PLACE causes robot to turn-in-place.

     @param finishingAction Dictates what should happen immediately after the target
     heading is reached (e.g. continue driving straight on target heading)

     @param completion Optional callback when target heading is reached or turn is
     aborted.
     */
    void turnToHeading(float targetHeading,
                       float radius,
                       CoreTurnFinishingAction finishingAction,
                       CoreTurnCompletion completion);


    /**
     Commands robot to drive until it reaches the target heading.  Robot will drive
     with the specified radius and speed.

     @param targetHeading The absolute heading (in degrees) that is the target
     [-180, 180]

     @param radius The radius at which the robot should drive, on the interval
     [-Inf, Inf]
     - RM_DRIVE_RADIUS_TURN_IN_PLACE causes robot to turn-in-place.
     - Positive radii cause robot to arc counter-clocwise when robot is driving
     forwards, unless forceTurnDirection is YES

     @param speed How fast the robot should move in meters / second. Negative
     speeds cause the robot to drive backwards.

     @param forceShortestTurn A flag indicating if the sign of the radius/speed
     should determine the direction to turn, or if the shortest turn direction
     should be used.

     @param finishingAction Dictates what should happen immediately after the target
     heading is reached (e.g. continue driving straight on target heading)

     @param completion Optional callback when target heading is reached or turn is
     aborted.
     */
    void turnToHeading(float targetHeading,
                       float radius,
                       float speed,
                       boolean forceShortestTurn,
                       CoreTurnFinishingAction finishingAction,
                       CoreTurnCompletion completion);


    void driveWithHeading(float heading, float power);


}
