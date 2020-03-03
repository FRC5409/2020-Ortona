package org.frc.team5409.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;
import org.frc.team5409.robot.Constants;


public final class CalibrateTurret extends CommandBase {
    private final ShooterTurret m_turret;

    public CalibrateTurret(ShooterTurret sys_rotation) {
        m_turret = sys_rotation;

        addRequirements(sys_rotation);
    }

    @Override
    public void initialize() {
        m_turret.enable();
        m_turret.setSafety(false);
        
        m_turret.setRaw(Constants.ShooterControl.shooter_calibrate_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }

    @Override
    public boolean isFinished() {
        var type = m_turret.getActiveResetSwitch();
        if (type != ShooterTurret.ResetSwitchType.kNone) {
            m_turret.resetRotation(type);
            return true;
        }

        return !m_turret.isEnabled();
    }
}