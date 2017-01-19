package cc.stargroup.xiaodai.shared;

public class CircleMath {
    public final static int CW = 1;
    public final static int  CCW = -1;

    public final static int MAX_CIRCLE_ANGLE = 180;
    public final static int MIN_CIRCLE_ANGLE = -180;

    // subtract two angles while making sure the results remains within the bounds
    // of the a single cycle of the circle
    // Note: assumes inputs are within MAX_CIRCLE/MIN_CIRCLE_ANGLE limits
    public static float circleSubtract(float a, float b) {
        return enforceCircularRange((a - b));
    }

    // add two angles while making sure the results remains within the bounds
    // of the a single cycle of the circle
    // Note: assumes inputs are within MAX_CIRCLE/MIN_CIRCLE_ANGLE limits
    public static float circleAdd(float a, float b) {
        return enforceCircularRange((a + b));
    }

    // take care of circle wrap-around (a and b are on the range
    // [MIN_CIRCLE_ANGLE, MAX_CIRCLE_ANGLE])
    // Note: assumes input angle <= 720 degrees
    public static float enforceCircularRange(float angle) {
        while (angle > MAX_CIRCLE_ANGLE) {angle -= 360;}
        while (angle < MIN_CIRCLE_ANGLE) {angle += 360;}

        return angle;
    }
}
