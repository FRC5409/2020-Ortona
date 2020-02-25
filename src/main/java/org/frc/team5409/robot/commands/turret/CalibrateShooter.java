package org.frc.team5409.robot.commands.turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.turret.*;
import org.frc.team5409.robot.subsystems.turret.ShooterTurret.ResetSwitchType;

public final class CalibrateShooter extends CommandBase {
    private final ShooterTurret m_turret;

    public CalibrateShooter(ShooterTurret sys_rotation, ShooterFlywheel sys_flywheel) {
        m_turret = sys_rotation;

        addRequirements(sys_rotation, sys_flywheel);
    }

    @Override
    public void initialize() {
        m_turret.enable();
        
        m_turret.setRaw(Constants.ShooterControl.shooter_calibrate_speed);
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