package frc.robot.util;

/**
 * Simple 3D Cartesian plane
 * point / vector / coordinate.
 * 
 * @author Keith Davies
 */
public class Vec3 {
    /**
     * Construct Blank Vec3.
     */
    public Vec3() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Construct Vec3 with coordinates.
     * 
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Compute magnitude of this vector.
     * |A|
     * 
     * @return Magnitude of this vector
     */
    public double magnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Compute normal of this vector.
     * 
     * @return Normal of this vector
     */ 
    public Vec3 norm() {
        final double mag = magnitude();
        return new Vec3(x / mag, y / mag, z / mag);
    }


    /**
     * X Coordinate
     */
    public double x;
    
    /**
     * Y Coordinate
     */
    public double y;
    
    /**
     * Z Coordinate
     */
    public double z;
}

