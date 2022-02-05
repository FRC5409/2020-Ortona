package org.frc.team5409.robot.training.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrainerDashboard {
    private final TrainingContext _context;

    public TrainerDashboard(TrainingContext context) {
        _context = context;
    }

    public void update() {
        Setpoint target = _context.getTargetSetpoint();
        SmartDashboard.putNumber("Setpoint Target", target.getTarget());
        SmartDashboard.putString("Setpoint Type", target.getType().name());
        SmartDashboard.putNumber("Setpoint Range Max", target.getRange().max());
        SmartDashboard.putNumber("Setpoint Range Min", target.getRange().min());

        TrainingModel model = _context.getModel();
        SmartDashboard.putNumber("Training Model kA", model.kA);
        SmartDashboard.putNumber("Training Model kB", model.kB);
        SmartDashboard.putNumber("Training Model kC", model.kC);
        SmartDashboard.putNumber("Training Model kD", model.kD);
        
        SmartDashboard.putNumber("Estimated Distance", _context.getDistance());
    }

    public void sync() {
        double newSetpointTarget = SmartDashboard.getNumber("Setpoint Target", 0.0);

        Setpoint setpoint = _context.getTargetSetpoint();
        if (newSetpointTarget != setpoint.getTarget()) {
            newSetpointTarget = setpoint.getRange().limit(newSetpointTarget);
            _context.setTargetSetpoint(
                new Setpoint(setpoint.getParent(), newSetpointTarget, setpoint.getRange(), setpoint.getType())
            );
        }
    }
}
