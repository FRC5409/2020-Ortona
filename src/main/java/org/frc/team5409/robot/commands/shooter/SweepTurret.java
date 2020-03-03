package org.frc.team5409.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;

import org.frc.team5409.robot.subsystems.shooter.*;
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
public final class SweepTurret extends CommandBase {
    private final ShooterTurret   m_turret;

    private final SimpleEquation  m_smooth_sweep, m_smooth_sweep_inverse;

    private       double          m_timer, m_smooth_sweep_toff;

    public SweepTurret(ShooterTurret sys_turret, ShooterFlywheel sys_flywheel) {
        m_turret = sys_turret;

        m_smooth_sweep_inverse = Constants.ShooterControl.shooter_smooth_sweep_inverse;
        m_smooth_sweep = Constants.ShooterControl.shooter_smooth_sweep_func;
    
        addRequirements(sys_flywheel, sys_turret);
    }

    @Override
    public void initialize() {
        m_turret.enable();

        m_smooth_sweep_toff = m_smooth_sweep_inverse.calculate(m_turret.getRotation());
        m_timer = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        double time = Timer.getFPGATimestamp() - m_timer;
        
        m_turret.setRotation(m_smooth_sweep.calculate(time + m_smooth_sweep_toff));
    }

    @Override
    public void end(boolean interrupted) {
        m_turret.disable();
    }
    
    @Override
    public boolean isFinished() {
        return !m_turret.isEnabled();
    }
}