package org.frc.team5409.robot.commands.shooter;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;

import edu.wpi.first.wpilibj2.command.CommandBase;

public final class ResetFlywheelOffset extends CommandBase {
    private final ShooterFlywheel m_flywheel;

    public ResetFlywheelOffset(ShooterFlywheel sys_flywheel) {
        m_flywheel = sys_flywheel;
        addRequirements(sys_flywheel);
    }

    @Override
    public void initialize() {
        m_flywheel.shiftOffset(-m_flywheel.getVelocityOffset());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}