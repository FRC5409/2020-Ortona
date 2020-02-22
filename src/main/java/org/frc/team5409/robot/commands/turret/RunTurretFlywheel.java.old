package org.frc.team5409.robot.commands.turret;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.turret.TurretFlywheel;
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
public final class RunTurretFlywheel extends CommandBase {
    private final TurretFlywheel m_turret;
    private final Indexer        m_indexer;
    private final Limelight      m_limelight;

    private final double         m_target_height;
    private       double         m_target,
                                 m_distance;

    private final Range          m_distance_range;
    private final SimpleEquation m_rpm_curve;

    /**
     * Constructs a Run Turret command.
     * 
     * @param subsystem The turret flywheel subsystem.
     * @param target    The target angle.
     * 
     * @author Keith Davies
     */
    public RunTurretFlywheel(TurretFlywheel sys_flywheel, Indexer sys_indexer, Limelight sys_limelight) {
        m_turret = sys_flywheel;
        m_indexer = sys_indexer;
        m_limelight = sys_limelight;

        m_target_height = Math.abs(Constants.Vision.vision_outerport_height - Constants.Vision.vision_limelight_height);

        m_distance_range = Constants.TurretControl.turret_distance_range;
        m_rpm_curve = Constants.TurretControl.turret_distance_rpm_curve;

        addRequirements(sys_flywheel, sys_indexer);
    }

    @Override
    public void initialize() {
        m_turret.enable();

        m_target = 0;
        m_distance = 0;
        
        if (!m_limelight.isEnabled()) {
            m_limelight.enable();
            m_limelight.setLedMode(Limelight.LedMode.kModeOn);
        }

        m_turret.startFeeder();
    }

    @Override
    public void execute() {
        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            m_distance = m_distance_range.clamp(m_target_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch)));
            m_target = m_rpm_curve.calculate(m_distance);

            m_turret.setVelocity(m_target);
        }
        
        SmartDashboard.putNumber("Target Velocity", m_target);
        SmartDashboard.putNumber("Actual Velocity", m_turret.getVelocity());
        SmartDashboard.putNumber("Distance (ft)", m_distance);
        if (m_turret.getVelocity() > m_target*Constants.TurretControl.turret_flywheel_target_thresh)
            m_indexer.moveIndexerMotor(-0.8);
        else
            m_indexer.moveIndexerMotor(0);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
        m_limelight.disable();
        
        m_indexer.moveIndexerMotor(0);
    }
    
    @Override
    public boolean isFinished() {
        return !m_turret.isEnabled();
    }
}