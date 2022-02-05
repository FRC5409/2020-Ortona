package org.frc.team5409.robot.commands.trainer;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.training.robot.Setpoint;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;

public class UndoTargetSetpoint extends CommandBase {
    private final TrainingContext _context;
    private final TrainerDashboard _dasboard;

    public UndoTargetSetpoint(TrainerDashboard dashboard, TrainingContext context) {
        _context = context;
        _dasboard = dashboard;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        Setpoint targetParent = _context.getSetpoint().getParent();
        if (targetParent != null)
            _context.setSetpoint(targetParent);
        
        _dasboard.update();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
