package org.frc.team5409.robot.commands.trainer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

import org.frc.team5409.robot.subsystems.shooter.*;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * This command runs the turret flywheel at a speed
 * porportional to the distance of the turret from the
 * shooting target and operates the indexer once the rpm
 * reaches it's setpoint.
 * 
 * @author Keith Davies
 */
public final class TrainerAlignTurret extends CommandBase {
    private final ShooterTurret m_shooter_turret;
    private final Limelight m_limelight;
    private final TrainerDashboard m_dashboard;
    private final TrainingContext m_training_context;

    public TrainerAlignTurret(TrainingContext context, TrainerDashboard dashboard, ShooterTurret sys_rotation, Limelight sys_limelight) {
        m_shooter_turret = sys_rotation;
        m_limelight = sys_limelight;
        m_dashboard = dashboard;
        m_training_context = context;
    
        addRequirements(sys_rotation, sys_limelight);
    }

    @Override
    public void initialize() {
        m_shooter_turret.enable();

        m_limelight.enable();
        m_limelight.setLedMode(Limelight.LedMode.kModeOn);
    }

    @Override
    public void execute() {
        Vec2 target = m_limelight.getTarget();

        m_shooter_turret.setRotation(m_shooter_turret.getRotation()+target.x);
        
        m_training_context.setDistance(
            Constants.Vision.vision_outerport_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch))
        );
    }

    @Override
    public void end(boolean interrupted) {
        m_limelight.disable();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}