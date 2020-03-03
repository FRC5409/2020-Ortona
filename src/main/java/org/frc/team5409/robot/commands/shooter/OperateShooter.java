package org.frc.team5409.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc.team5409.robot.subsystems.shooter.*;
import org.frc.team5409.robot.subsystems.*;
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
public final class OperateShooter extends CommandBase {
    /**
     * The state of the OperareTurret command
     * during it's execution.
     */
    private enum CommandState {
        kSearching, kSweeping, kAligning, kShooting, kEnd
    }

    private final ShooterFlywheel m_shooter_flywheel;
    private final ShooterTurret   m_shooter_turret;
    private final Limelight       m_limelight;
    private final Indexer         m_indexer;

    private final SimpleEquation  m_rpm_curve;
    private final SimpleEquation  m_smooth_sweep, m_smooth_sweep_inverse;

    private final double          m_port_height;
    private final Range           m_distance_range;

    private       CommandState    m_state;
    private       double          m_distance, m_timer;
    private       double          m_smooth_sweep_toff;

    public OperateShooter(ShooterFlywheel sys_flywheel, ShooterTurret sys_rotation, Limelight sys_limelight, Indexer sys_indexer) {
        m_shooter_flywheel = sys_flywheel;
        m_shooter_turret = sys_rotation;
        m_limelight = sys_limelight;
        m_indexer = sys_indexer;

        m_distance_range = Constants.ShooterControl.shooter_distance_range;
        m_port_height = Math.abs(Constants.Vision.vision_outerport_height - Constants.Vision.vision_limelight_height);

        m_smooth_sweep_inverse = Constants.ShooterControl.shooter_smooth_sweep_inverse;
        m_smooth_sweep = Constants.ShooterControl.shooter_smooth_sweep_func;
        m_rpm_curve = Constants.ShooterControl.shooter_distance_rpm_curve;
    
        addRequirements(sys_flywheel, sys_rotation, sys_limelight, sys_indexer);
    }

    @Override
    public void initialize() {
        m_shooter_flywheel.enable();
        m_shooter_flywheel.setSafety(false);

        m_shooter_turret.enable();
        m_shooter_turret.setSafety(false);

        m_limelight.enable();
        m_limelight.setLedMode(Limelight.LedMode.kModeOn);

        m_shooter_flywheel.startFeeder();
        m_shooter_flywheel.setVelocity(m_rpm_curve.calculate(m_distance_range.median));

        internal_switchState(CommandState.kSearching);
    }

    @Override
    public void execute() {
        double time = Timer.getFPGATimestamp() - m_timer;
        switch (m_state) {
            case kSearching:
                internal_operateSearching(time);
                break;
            case kSweeping:
                internal_operateSweeping(time);
                break;
            case kAligning:
                internal_operateAligning(time);
                break;
            case kShooting:
                internal_operateShooting(time);
                break;
            case kEnd:
                end(true);
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_shooter_flywheel.disable();
        m_shooter_turret.disable();
        m_limelight.disable();
        
        m_indexer.moveIndexerMotor(0);
    }
    
    @Override
    public boolean isFinished() {
        return m_state == CommandState.kEnd;
    }

    /**
     * Switches the internal state of the command and
     * initialize vairables and subsystems for certain
     * states if necessary.
     */
    private void internal_switchState(CommandState state) {
        m_timer = Timer.getFPGATimestamp();
        m_state = state;

        if (state == CommandState.kSweeping) {
            m_smooth_sweep_toff = m_smooth_sweep_inverse.calculate(m_shooter_turret.getRotation());
        } else if (state == CommandState.kShooting) {
            Vec2 target = m_limelight.getTarget();
            m_limelight.disable();

            m_distance = m_port_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch));

            SmartDashboard.putNumber("Predicted Velocity", m_rpm_curve.calculate(m_distance));
            SmartDashboard.putNumber("Robot Distance (ft)", m_distance);

            m_shooter_flywheel.setVelocity( m_rpm_curve.calculate(m_distance));
            //m_shooter_flywheel.setVelocity(SmartDashboard.getNumber("Target Velocity", 0));
        }
    }

    /**
     * <h3>Operates the turret in the "Searching" state.</h3>
     * 
     * <p> In this state, the turret waits for the limelight led's to
     * turn on and checks to see if there are any targets. There's always
     * a chance that the limelight may detect a target the first time it's
     * led's turn since the robot should be generally facing it's target
     * upon shooting, and this state checks for that possibility. If there
     * is no target by the time the aqusition delay time runs, it switches
     * over to the {@code kSweeping} state.</p>
     * 
     * @param time The time since the this state began execution.
     */
    private void internal_operateSearching(double time) {
        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            internal_switchState(CommandState.kAligning);
        } else if (time > Constants.Vision.vision_acquisition_delay) {
            internal_switchState(CommandState.kSweeping);
        }
    }

    /**
     * <h3>Operates the turret in the "Sweeping" state.</h>
     * 
     * <p>In this state, the turret sweeps about it's axis
     * smoothly using a scaled cosine function. If at any point the
     * limelight detects a target, it switches over to the {@code kShooting}
     * state. If there is no target after "x" amount of sweeps, (See Constants)
     * the command cancels itself. The reason for this is due to the fact
     * that the limelight's led's may not be on for prolonged periods of time.</p>
     * 
     * @param time The time since the this state began execution.
     */
    private void internal_operateSweeping(double time) {
        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            internal_switchState(CommandState.kAligning);
        } else if (time / Constants.ShooterControl.shooter_smooth_sweep_period > Constants.ShooterControl.shooter_smooth_sweep_max_sweeps) {
            internal_switchState(CommandState.kEnd);
        } else {
            m_shooter_turret.setRotation(m_smooth_sweep.calculate(time+m_smooth_sweep_toff));
        }
    }

    private void internal_operateAligning(double time) {
        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            m_shooter_turret.setRotation(m_shooter_turret.getRotation()+target.x);
            if (Math.abs(target.x) < Constants.Vision.vision_aligned_thresh)
                internal_switchState(CommandState.kShooting);
            
            SmartDashboard.putNumber("Aligning offset", target.x);
        }
    }

    /**
     * <h3>Operates the turret in the "Shooting" state.</h>
     * 
     * <p>In this state, the turret runs it's flywheel at a speed 
     * porportional to the distance of the turret from the outer port
     * and aligns the turret's rotation axis to the target.
     * Once both systems have reached their respective targets,
     * the indexer triggers, feeding powercells into the turret.</p>
     * 
     * @param time The time since the this state began execution.
     */
    private void internal_operateShooting(double time) {
        SmartDashboard.putBoolean("Turret target reached", m_shooter_turret.isTargetReached());
        SmartDashboard.putBoolean("Shooter target reached", m_shooter_flywheel.isTargetReached());
        if (m_shooter_turret.isTargetReached() && m_shooter_flywheel.isTargetReached()) {
            m_indexer.moveIndexerMotor(1);
        } else {
            m_indexer.moveIndexerMotor(0);
        }
    }
}