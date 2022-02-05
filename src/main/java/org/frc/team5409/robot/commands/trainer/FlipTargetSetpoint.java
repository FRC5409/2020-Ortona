package org.frc.team5409.robot.commands.trainer;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.training.robot.Setpoint;
import org.frc.team5409.robot.training.robot.SetpointType;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;

public class FlipTargetSetpoint extends CommandBase {
    private final TrainingContext _context;
    private final TrainerDashboard _dasboard;

    public FlipTargetSetpoint(TrainerDashboard dashboard, TrainingContext context) {
        _context = context;
        _dasboard = dashboard;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {    
        Setpoint target = _context.getSetpoint();
        
        if (target.getType() == SetpointType.LEFT)
            _context.setSetpoint(target.getParent().branch(false));
        else if (target.getType() == SetpointType.RIGHT)
            _context.setSetpoint(target.getParent().branch(true));
        
        _dasboard.update();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
