package org.frc.team5409.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.subsystems.turret.ShooterTurret;

/**
 * Turns turret to {@code target} angle.
 * 
 * @author Keith Davies
 */
public final class RotateTurret extends CommandBase {
    private final ShooterTurret m_turret;

    private final double         m_target;

    /**
     * Constructs Turret Rotation command.
     * 
     * @param subsystem The turret rotation subsystem.
     * @param target    The target angle.
     */
    public RotateTurret(ShooterTurret subsystem, double target) {
        m_turret = subsystem;
        m_target = target;

        addRequirements(m_turret);
    }

    @Override
    public void initialize() {
        m_turret.enable();
    }

    @Override
    public void execute() {
        m_turret.setRotation(m_target);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }
    
    @Override
    public boolean isFinished() {
        return m_turret.isTargetReached();
    }
}