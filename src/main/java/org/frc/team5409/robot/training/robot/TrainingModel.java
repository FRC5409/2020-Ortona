package org.frc.team5409.robot.training.robot;

import org.frc.team5409.robot.Constants;

public class TrainingModel {
    private static final Range SCALAR = new Range(0, 1);

    public final double kA;
    public final double kB;
    public final double kC;
    public final double kD;
    
    public TrainingModel(double kA, double kB, double kC, double kD) {
        this.kA = kA;
        this.kB = kB;
        this.kC = kC;
        this.kD = kD;
    }

    public double calculate(double x) {
        x = SCALAR.limit(x / Constants.Training.MAX_DISTANCE);
        double x2 = x*x;
        double x3 = x2*x;
        return (kA*x3*x + kB*x3 + kC*x2 + kD*x) * Constants.Training.MAX_SPEED;
    }
}
