package org.frc.team5409.robot.training.robot;

public class TrainingContext {
    private TrainingModel _model;
    private Setpoint _target;
    private Setpoint _active;
    private double _distance;

    public TrainingContext(Setpoint initialTarget) {
        _model = new TrainingModel(0.0, 0.0, 0.0, 0.0);
        _target = initialTarget;
        _active = initialTarget;
        _distance = 0.0;
    }

    public void setModel(TrainingModel model) {
        _model = model;
    }
    
    public void setTargetSetpoint(Setpoint target) {
        _target = target;
    }
    
    public void setActiveSetpoint(Setpoint target) {
        _active = target;
    }
    
    public void setDistance(double distance) {
        _distance = distance;
    }

    public TrainingModel getModel() {
        return _model;
    }

    public Setpoint getTargetSetpoint() {
        return _target;
    }

    public Setpoint getActiveSetpoint() {
        return _active;
    }

    public double getDistance() {
        return _distance;
    }
}
