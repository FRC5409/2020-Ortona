package org.frc.team5409.robot.training.robot;

public class TrainingContext {
    private TrainingModel _model;
    private Setpoint _target;
    private double _distance;

    public TrainingContext(Setpoint initialTarget) {
        _model = new TrainingModel(0.0, 0.0, 0.0, 0.0);
        _target = initialTarget;
        _distance = 0.0;
    }

    public void setModel(TrainingModel model) {
        _model = model;
    }
    
    public void setSetpoint(Setpoint target) {
        _target = target;
    }
    
    public void setDistance(double distance) {
        _distance = distance;
    }

    public TrainingModel getModel() {
        return _model;
    }

    public Setpoint getSetpoint() {
        return _target;
    }

    public double getDistance() {
        return _distance;
    }
}
