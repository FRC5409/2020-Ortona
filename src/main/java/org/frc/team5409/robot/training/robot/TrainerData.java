package org.frc.team5409.robot.training.robot;

public class TrainerData {
    private final double _target;
    private final double _distance;

    public TrainerData(double target, double distance) {
        _target = target;
        _distance = distance;
    }
    
    public double getDistance() {
        return _distance;
    }

    public double getTarget() {
        return _target;
    }
}
