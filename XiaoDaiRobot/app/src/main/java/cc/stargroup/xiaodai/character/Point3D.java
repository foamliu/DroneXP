package cc.stargroup.xiaodai.character;

/**
 @struct RMPoint3D
 @brief A helper data type for reasoning about 3-Dimensional cartesian space
 within the RMCharacter framework
 @var RMPoint3D::x
 The X-axis component of the point
 @var RMPoint3D::y
 The Y-axis component of the point
 @var RMPoint3D::z
 The Z-axis component of the point
 */
public class Point3D {
    public float x;
    public float y;
    public float z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
