package org.frc.team5409.robot.training.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TrainerDashboard {
    private final TrainingContext _context;

    public TrainerDashboard(TrainingContext context) {
        _context = context;
    }

    public void update() {
        SmartDashboard.putNumber("Setpoint Target", _context.getTargetSetpoint().getTarget());
        SmartDashboard.putNumber("Setpoint Range Max", _context.getTargetSetpoint().getRange().max());
        SmartDashboard.putNumber("Setpoint Range Min", _context.getTargetSetpoint().getRange().min());
        SmartDashboard.putNumber("Estimated Distance", _context.getDistance());

        TrainingModel model = _context.getModel();
        SmartDashboard.putNumber("Training Model kA", model.kA);
        SmartDashboard.putNumber("Training Model kB", model.kB);
        SmartDashboard.putNumber("Training Model kC", model.kC);
        SmartDashboard.putNumber("Training Model kD", model.kD);
    }

    public void sync() {
        double newSetpointTarget = SmartDashboard.getNumber("Setpoint Target", 0.0);

        Setpoint setpoint = _context.getTargetSetpoint();
        if (newSetpointTarget != setpoint.getTarget()) {
            newSetpointTarget = setpoint.getRange().limit(newSetpointTarget);
            _context.setTargetSetpoint(
                new Setpoint(setpoint.getParent(), newSetpointTarget, setpoint.getRange())
            );
        }
    }
}
