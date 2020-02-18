package frc.robot.util;

/**
 * Stores PID coefficients for late
 * use in the program.
 * 
 * @author Keith Davies
 */
public final class PIDFConfig {
    public final double P, I, D, F;

    /**
     * Constructs a PID configuration.
     * 
     * @param P The proportional coefficient.
     * @param I The integral coefficient.
     * @param D The derivative coefficient.
     * @param F The feed-forward coefficient.
     */
    public PIDFConfig(double P, double I, double D, double F) {
        this.P = P;
        this.I = I;
        this.D = D;
        this.F = F;
    }
}