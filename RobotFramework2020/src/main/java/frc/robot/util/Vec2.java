package frc.robot.util;

/**
 * Simple 2D Cartesian plane
 * point / vector / coordinate.
 * 
 * @author Keith Davies
 */
public class Vec2 {
    /**
     * Construct Blank Vec2.
     */
    public Vec2() {
        x = 0;
        y = 0;
    }

    /**
     * Construct Vec2 with coordinates.
     * 
     * @param x X Coordinate
     * @param y Y Coordinate
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Compute dot product of this vector with
     * another. (A * B)
     * 
     * @param B Another Vector
     * 
     * @return Dot product
     */
    public double dot(Vec2 B) {
        return (x*B.x + y*B.y);
    }

    /**
     * Compute magnitude of this vector.
     * |A|
     * 
     * @return Magnitude of this vector
     */
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Returns normalization of this vector.
     * A'
     * 
     * @return Magnitude of this vector
     */
    public Vec2 normalize() {
        final double mag = magnitude();
        return new Vec2(x/mag, y/mag);
    }

    /**
     * X Coordinate
     */
    public double x;
    
    /**
     * Y Coordinate
     */
    public double y;
}
