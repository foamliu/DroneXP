package cc.stargroup.xiaodai.shared;

/* --Constants-- */
enum controlAxisNames {
    ROLL,
    PITCH,
    YAW
};

/* --Data Types-- */
class EulerAngles {
    double roll;
    double pitch;
    double yaw;
}

public class Quaternion {

    private double w, x, y, z;

/* --Public Methods-- */

    // factory methods to create an initialized quaternion
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(Quaternion q) {
        this.w = q.w;
        this.x = q.x;
        this.y = q.y;
        this.z = q.z;
    }

    public Quaternion() {

    }

    private void setQuaternionParameters(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // find magnitude of quaternion
    public double magnitudeOfQuaternion(Quaternion q) {
        return java.lang.Math.sqrt(q.w * q.w +
                q.x * q.x +
                q.y * q.y +
                q.z * q.z);
    }

    // normalize quaternion
    public Quaternion normalizeQuaternion(Quaternion q) {
        double magnitude = magnitudeOfQuaternion(q);

        q.w /= magnitude;
        q.x /= magnitude;
        q.y /= magnitude;
        q.z /= magnitude;

        return q;
    }

    // same as inverse for normalized quaternion
    public Quaternion conjugateQuaternion(Quaternion q) {
        q.x = -q.x;
        q.y = -q.y;
        q.z = -q.z;

        return q;
    }

    // ensures quaterion is normalized
    public Quaternion inverseQuaternion(Quaternion q) {
        q = normalizeQuaternion(q);

        return conjugateQuaternion(q);
    }

    public Quaternion quaternionMultiply(Quaternion q1, Quaternion q2) {
        Quaternion q3 = new Quaternion();

        q3.w = q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z;
        q3.x = q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y;
        q3.y = q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x;
        q3.z = q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w;

        return q3;
    }

    public Quaternion scalarMultiply(Quaternion q, float s) {
        q.w *= s;
        q.x *= s;
        q.y *= s;
        q.z *= s;

        return q;
    }

    // find difference between two quaternions (q1 - q0)
    public Quaternion differenceFromQuaternion(Quaternion q0, Quaternion q1) {
        // take conjugate of starting quaternion
        q0 = conjugateQuaternion(q0);

        // find quaternion to get to ending quaternion (q1)
        return  quaternionMultiply(q1, q0);
    }


    public EulerAngles getEulerAnglesFromQuaternion(Quaternion q) // find euler representation of quaternion
    {
        EulerAngles euler = new EulerAngles();

        q = normalizeQuaternion(q);

        double test = q.y * q.z + q.x * q.w;

        // if singularity at north pole...
        if (test > 0.4999)  // 0.4999 corresponds to ~88 degrees
        {
            euler.yaw = 2 * java.lang.Math.atan2(q.y, q.w);
            euler.pitch = java.lang.Math.PI / 2;
            euler.roll = 0;
            return euler;
        }

        // if singularity at south pole...
        if (test < -0.4999) // 0.499 corresponds to ~88 degrees
        {
            euler.yaw = -2 * java.lang.Math.atan2(q.y, q.w);
            euler.pitch = -java.lang.Math.PI / 2;
            euler.roll = 0;
            return euler;
        }

        // otherwise:
        double sqx = q.x * q.x;
        double sqy = q.y * q.y;
        double sqz = q.z * q.z;

        euler.yaw = (float) java.lang.Math.atan2(2 * q.z * q.w - 2 * q.y * q.x, 1 - 2 * sqz -
                2 * sqx);

        euler.pitch = (float) java.lang.Math.asin(2 * test);

        euler.roll = (float) java.lang.Math.atan2(2 * q.y * q.w - 2 * q.z * q.x, 1 - 2 * sqy -
                2 * sqx);
        return euler;
    }

    public Quaternion getQuaternionFromEulerAnglesRollDeg(double roll, double pitch, double yaw) {
        return getQuaternionFromEulerAnglesRollRad(
                java.lang.Math.toRadians(roll),
                java.lang.Math.toRadians(pitch),
                        java.lang.Math.toRadians(yaw));
    }

    public Quaternion getQuaternionFromEulerAnglesRollRad(double roll, double pitch, double yaw) {
        Quaternion q = new Quaternion();

        double cy = java.lang.Math.cos(yaw / 2);
        double sy = java.lang.Math.sin(yaw / 2);
        double cp = java.lang.Math.cos(pitch / 2);
        double sp = java.lang.Math.sin(pitch / 2);
        double cr = java.lang.Math.cos(roll / 2);
        double sr = java.lang.Math.sin(roll / 2);

        q.w = (cy * cp * cr) - (sy * sp * sr);
        q.x = (cy * cr * sp) - (sy * cp * sr);
        q.y = (cy * cp * sr) + (sy * cr * sp);
        q.z = (cy * sp * sr) + (cp * cr * sy);

        return q;
    }

}
