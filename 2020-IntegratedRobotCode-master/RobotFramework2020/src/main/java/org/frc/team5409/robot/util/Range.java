package org.frc.team5409.robot.util;

/**
 * Holds a numerical range and provides
 * covenience functions for clamping
 * numbers.
 * 
 * @author Keith Davies
 */
public final class Range {
    public double min, max;

    /**
     * Constructs a range.
     * 
     * @param min The minimum range
     * @param max The maximum range
     */
    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Clamps a value between {@code min} and
     * {@code max}.
     * 
     * @param min   The minimum range
     * @param value The value
     * @param max   The maximum range
     * 
     * @return      The clamped value.
     */
    public static double clamp(double min, double value, double max) {
        if (value > max)
            return max;
        else if (value < min)
            return min;
        return value;
    }

    /**
     * Clamps a value between [-1, 1]
     * 
     * @param value The value
     * 
     * @return The clamped value.
     */
    public static double normalize(double value) {
        return clamp(-1, value, 1);
    }

    /**
     * Clamps a value between [0, 1]
     * 
     * @param value The value
     * 
     * @return The clamped value.
     */
    public static double scalar(double value) {
        return clamp(0, value, 1);
    }

    /**
     * Clamps a value to this range.
     * 
     * @param value The value
     * 
     * @return      The clamped value.
     */
    public double clamp(double value) {
        return clamp(min, value, max);
    }
}