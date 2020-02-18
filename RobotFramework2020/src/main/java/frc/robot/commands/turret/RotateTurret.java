package frc.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.turret.TurretRotation;

/**
 * Turns turret to {@code target} angle.
 * 
 * @author Keith Davies
 */
public final class RotateTurret extends CommandBase {
    private final TurretRotation m_turret;

    private final double         m_target;

    /**
     * Constructs Turret Rotation command.
     * 
     * @param subsystem The turret rotation subsystem.
     * @param target    The target angle.
     */
    public RotateTurret(TurretRotation subsystem, double target) {
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
        return m_turret.isAt(m_target);
    }
}