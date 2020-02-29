package org.frc.team5409.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.Indexer;
import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;

public final class ReverseIndexer extends CommandBase {
    private final Indexer m_indexer;

    /**
     * Constructs Turret Rotation command.
     * 
     * @param subsystem The turret rotation subsystem.
     */
    public ReverseIndexer(Indexer subsystem) {
        m_indexer = subsystem;
        addRequirements(m_indexer);
    }

    @Override
    public void initialize() {
        m_indexer.moveIndexerMotor(-1);
    }

    @Override
    public void end(boolean interrupted) {
        m_indexer.moveIndexerMotor(0);
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}