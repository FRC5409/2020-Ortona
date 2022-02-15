package org.frc.team5409.robot.commands.trainer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.training.robot.TrainingContext;

public class BranchTargetSetpoint extends CommandBase {
    private final TrainingContext _context;
    private final boolean _isLeft;

    public BranchTargetSetpoint(TrainingContext context, boolean isLeft) {
        _context = context;
        _isLeft = isLeft;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {    
        _context.setTargetSetpoint(
            _context.getTargetSetpoint().branch(_isLeft)
        );
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
