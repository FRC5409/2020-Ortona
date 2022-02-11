package org.frc.team5409.robot.commands.trainer;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.training.robot.SetpointType;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;

public class BranchTargetSetpoint extends CommandBase {
    private final TrainingContext _context;
    private final boolean _isLeft;
    private final TrainerDashboard _dasboard;

    public BranchTargetSetpoint(TrainerDashboard dashboard, TrainingContext context, boolean isLeft) {
        _context = context;
        _isLeft = isLeft;
        _dasboard = dashboard;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {    
        _context.setSetpoint(
            _context.getSetpoint().branch(_isLeft)
        );

        _dasboard.update();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
