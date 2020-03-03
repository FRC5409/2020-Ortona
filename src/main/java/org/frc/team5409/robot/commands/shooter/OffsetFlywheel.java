package org.frc.team5409.robot.commands.shooter;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;

import edu.wpi.first.wpilibj2.command.CommandBase;

public final class OffsetFlywheel extends CommandBase {
    private final ShooterFlywheel m_flywheel;

    private final double          m_increment;

    public OffsetFlywheel(ShooterFlywheel sys_flywheel, boolean increments) {
        m_flywheel = sys_flywheel;

        if (increments)
            m_increment = Constants.ShooterControl.shooter_flywheel_offset_increment;
        else
            m_increment = -Constants.ShooterControl.shooter_flywheel_offset_increment;
        //addRequirements(sys_flywheel);
    }

    @Override
    public void initialize() {
        m_flywheel.shiftOffset(m_increment);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}