package org.frc.team5409.robot.training.robot;

public class TrainingModel {
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
        double x2 = x*x;
        double x3 = x2*x;
        return kA*x3*x + kB*x3 + kC*x2 + kD*x;
    }
}
