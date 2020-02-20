package org.frc.team5409.robot.util;

/**
 * Simple interface for the representation of
 * simple input, output mathematical equations
 * such as polynomials.
 * 
 * @author Keith Davies
 */
@FunctionalInterface
public interface SimpleEquation {
    /**
     * Calculates the output of the equation
     * given input {@code x}.
     * 
     * @param x The input variable.
     * 
     * @return  The calculated output value.
     */
    public double calculate(double x);
}