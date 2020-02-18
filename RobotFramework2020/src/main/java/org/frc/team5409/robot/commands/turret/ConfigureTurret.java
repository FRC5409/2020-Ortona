package org.frc.team5409.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.turret.*;
import org.frc.team5409.robot.subsystems.turret.TurretRotation.LimitSwitchType;

public final class ConfigureTurret extends CommandBase {
    private final TurretRotation m_turret;

    public ConfigureTurret(TurretRotation m_turret_rotation, TurretFlywheel m_turret_flywheel) {
        m_turret = m_turret_rotation;

        addRequirements(m_turret_rotation, m_turret_flywheel);
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
        LimitSwitchType type = m_turret.getCurrentLimitSwitch();

        if (type != LimitSwitchType.kNone) {
            m_turret.resetRotation(type);
            return true;
        }

        return false;
    }
}