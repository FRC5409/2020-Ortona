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
public final class DriveByShooter extends CommandBase {
    /**
     * The state of the OperareTurret command
     * during it's execution.
     */
    private enum CommandState {
        kSearching, kSweeping, kAligning, kShooting, kEnd
    }

    private final ShooterFlywheel m_flywheel;
    private final ShooterTurret   m_turret;
    private final Limelight       m_limelight;
    private final Indexer         m_indexer;
    private final DriveTrain      m_drivetrain;

    private final SimpleEquation  m_rpm_curve;
    private final SimpleEquation  m_smooth_sweep, m_smooth_sweep_inverse;
    private final SimpleEquation  m_rpm_leniency_func;

    private final double          m_port_height;
    private final Range           m_distance_range;

    private       CommandState    m_state;
    private       double          m_distance, m_leniency, m_distance_vel;
    private       double          m_timer, m_timer_last;
    private       double          m_smooth_sweep_toff;

    private       Vec2            m_port_pos, m_robot_pos;

    public DriveByShooter(ShooterFlywheel sys_flywheel, ShooterTurret sys_rotation, Limelight sys_limelight, Indexer sys_indexer, DriveTrain sys_drivetrain) {
        m_flywheel = sys_flywheel;
        m_turret = sys_rotation;
        m_limelight = sys_limelight;
        m_indexer = sys_indexer;
        m_drivetrain = sys_drivetrain;

        m_distance_range = Constants.ShooterControl.shooter_distance_range;
        m_port_height = Math.abs(Constants.Vision.vision_outerport_height - Constants.Vision.vision_limelight_height);

        m_smooth_sweep_inverse = Constants.ShooterControl.shooter_smooth_sweep_inverse;
        m_rpm_leniency_func = Constants.ShooterControl.shooter_rpm_leniency_func;
        m_smooth_sweep = Constants.ShooterControl.shooter_smooth_sweep_func;
        m_rpm_curve = Constants.ShooterControl.shooter_distance_rpm_curve;

        m_port_pos = new Vec2();
        m_robot_pos = new Vec2();
    
        SmartDashboard.setDefaultNumber("Scalll", 0);

        addRequirements(sys_flywheel, sys_rotation, sys_limelight, sys_indexer);
    }

    @Override
    public void initialize() {
        m_flywheel.enable();

        //m_flywheel.startFeeder();
        m_flywheel.setVelocity(m_rpm_curve.calculate(m_distance_range.median));

        //m_turret.enable();

        m_limelight.enable();
        m_limelight.setLedMode(Limelight.LedMode.kModeOn);
        m_timer_last = 0;
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
                end(false);
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_flywheel.disable();
        m_turret.disable();
        m_limelight.disable();
        
        m_indexer.moveIndexerMotor(0);

        (new RotateTurret(m_turret, 0)).schedule();
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
            m_smooth_sweep_toff = m_smooth_sweep_inverse.calculate(m_turret.getRotation());
        }
    }

    private void internal_advanceIndexer() {
        if (!m_indexer.ballDetectionExit()) {
            m_indexer.moveIndexerMotor(1);
        } else {
            m_indexer.moveIndexerMotor(0);
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
        internal_advanceIndexer();
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
            internal_switchState(CommandState.kShooting);
        } else if (time / Constants.ShooterControl.shooter_smooth_sweep_period > Constants.ShooterControl.shooter_smooth_sweep_max_sweeps) {
            internal_switchState(CommandState.kEnd);
        } else {
            m_turret.setRotation(m_smooth_sweep.calculate(time+m_smooth_sweep_toff));
        }
        internal_advanceIndexer();
    }

    @Deprecated
    private void internal_operateAligning(double time) {
        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            m_turret.setRotation(m_turret.getRotation()+target.x);
            if (Math.abs(target.x) < Constants.Vision.vision_aligned_thresh)
                internal_switchState(CommandState.kShooting);
            
            SmartDashboard.putNumber("Aligning offset", target.x);
        }
        internal_advanceIndexer();
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
        //double time_diff = time - m_timer_last;
        //m_timer_last = time;    

        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            m_distance = m_port_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch));
            //m_distance_vel = (distance - m_distance) / (time_diff);    

            m_leniency = m_rpm_leniency_func.calculate(m_distance);
            //m_distance = distance;

            if (target.x < Constants.ShooterControl.shooter_turret_target_thresh) {
                double heading = m_drivetrain.getHeading()/180*Math.PI;
                
                Vec2 direction = new Vec2(-Math.sin(heading), Math.cos(heading));

                m_port_pos = new Vec2(
                    

                );

                SmartDashboard.putNumber("heading", m_drivetrain.getHeading());
                SmartDashboard.putString("Direction", String.format("{%f, %f}", direction.x, direction.y));
            }

            SmartDashboard.putNumber("Velo", -m_drivetrain.getAverageEncoderRate()*SmartDashboard.getNumber("Scalll", 1));

            m_flywheel.setVelocity(m_rpm_curve.calculate(m_distance));
            m_turret.setRotation(m_turret.getRotation()+target.x+m_drivetrain.getAverageEncoderRate()*12);
        }

        if (m_turret.isTargetReached() && Math.abs(m_flywheel.getVelocity()-m_flywheel.getTarget()) < m_leniency) {
            m_indexer.moveIndexerMotor(1);
        } else if (!m_indexer.ballDetectionExit()) {
            m_indexer.moveIndexerMotor(1);
        } else {
            m_indexer.moveIndexerMotor(0);
        }
    }
}