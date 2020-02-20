package org.frc.team5409.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.turret.*;
import org.frc.team5409.robot.subsystems.turret.TurretRotation.ResetSwitchType;

public final class ConfigureTurret extends CommandBase {
    private final TurretRotation m_turret;

    public ConfigureTurret(TurretRotation turret_rotation, TurretFlywheel turret_flywheel) {
        m_turret = turret_rotation;

        addRequirements(turret_rotation, turret_flywheel);
    }

    @Override
    public void initialize() {
        m_turret.enable();
        
        m_turret.setRaw(Constants.TurretControl.turret_sweep_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }

    @Override
    public boolean isFinished() {
        ResetSwitchType type = m_turret.getActiveResetSwitch();

        if (type != ResetSwitchType.kNone) {
            m_turret.resetRotation(type);
            return true;
        }

        return !m_turret.isEnabled();
    }
}