package org.frc.team5409.robot.commands.turret;

import java.time.Instant;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

import org.frc.team5409.robot.subsystems.turret.TurretFlywheel;
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
public final class RunTurretFlywheel extends CommandBase {
    private final TurretFlywheel m_turret;
    private final Limelight      m_limelight;
    private final Indexer        m_indexer;
    
    private final double         m_target_height;

    private final Range          m_distance_range;
    private final SimpleEquation m_rpm_curve;

    private       double         m_target, m_distance, m_timer;

    private       boolean        m_debounce1, m_debounce2, m_debounce3, m_debounce4;

    private       JoystickButton m_trg_flywheel, m_trg_score;

    private       Logger         m_log_current, m_log_velocity, m_log_events, m_log_scored;

    public RunTurretFlywheel(TurretFlywheel sys_flywheel, Indexer sys_indexer, Limelight sys_limelight, XboxController joy_main, XboxController joy_sec) {
        m_turret = sys_flywheel;
        m_indexer = sys_indexer;
        m_limelight = sys_limelight;

        m_distance = 0;
        m_target = 0;
        m_timer = 0;

        m_trg_flywheel = new JoystickButton(joy_main, 1);
        m_trg_score = new JoystickButton(joy_sec, 1);

        m_target_height = Math.abs(Constants.Vision.vision_outerport_height - Constants.Vision.vision_limelight_height);
        m_distance_range = Constants.TurretControl.turret_distance_range;
        m_rpm_curve = Constants.TurretControl.turret_distance_rpm_curve;

        SmartDashboard.setDefaultNumber("Target Velocity", 0);
        SmartDashboard.setDefaultNumber("Target Indexer", 0.8);
        SmartDashboard.setDefaultNumber("Feeder Threshold", 0.9);

        addRequirements(m_turret, m_indexer, m_limelight);
    }

    @Override
    public void initialize() {
        m_turret.enable();
        
        if (!m_limelight.isEnabled()) {
            m_limelight.enable();
            m_limelight.setLedMode(Limelight.LedMode.kModeOn);
        }

        String logs_path = "flywheel/"+Long.toString(Instant.now().getEpochSecond());

        m_log_velocity = new Logger(logs_path+"/FLYWHEEL_VELOCITY.csv");
        m_log_current = new Logger(logs_path+"/FLYWHEEL_CURRENT.csv");
        m_log_scored = new Logger(logs_path+"/POWERCELL_SCORES.csv");
        m_log_events = new Logger(logs_path+"/TURRET_EVENTS.csv");

        new Logger(logs_path+"/TURRET_CONSTANTS.csv")
            .writeln("%f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f",
                Constants.TurretControl.pid_turret_flywheel.P,
                Constants.TurretControl.pid_turret_flywheel.I,
                Constants.TurretControl.pid_turret_flywheel.D,
                Constants.TurretControl.pid_turret_flywheel.F,

                Constants.TurretControl.turret_flywheel_current_limit,
                Constants.TurretControl.turret_feeder_current_limit,

                Constants.TurretControl.turret_flywheel_rpm_scale,
                Constants.TurretControl.turret_flywheel_target_thresh,

                Constants.Vision.vision_limelight_height,
                Constants.Vision.vision_outerport_height,
                Constants.Vision.vision_limelight_pitch
            )
            .save();

        m_distance = 0;

        m_debounce1 = false;
        m_debounce2 = false;
        m_debounce3 = false;
        m_debounce4 = false;

        m_timer = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        double velocity = m_turret.getVelocity();
        double time = Timer.getFPGATimestamp() - m_timer;

        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            m_distance = m_distance_range.clamp(m_target_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch)));

            if (!m_debounce1)
                m_log_events.writeln("%f, TARGET AQUIRED [%f], %f", time, m_distance, m_distance);

            m_debounce1 = true;
        } else {

            if (m_debounce1)
                m_log_events.writeln("%f, TARGET LOST [%f], %f", time, m_distance, m_distance);
            
            m_distance = -1;

            m_debounce1 = false;
        }

        if (velocity > m_target*Constants.TurretControl.turret_flywheel_target_thresh) {
            m_indexer.moveIndexerMotor(-0.8);

            if (!m_debounce2) {
                m_log_events.writeln("%f, TURRET AT SPEED [%f], %f", time, velocity, velocity);
                m_log_events.writeln("%f, INDEXER STARTED [%f], %f", time, 0.8, 0.8);
            }
            
            m_debounce2 = true;
        } else {
            m_indexer.moveIndexerMotor(0);

            if (m_debounce2)
                m_log_events.writeln(String.format("%f, INDEXER STOPPED [%f], %f", time, 0, 0));

            m_debounce2 = false;
        }
        
        m_log_velocity.writeln("%f, %f", time, velocity);
        m_log_current.writeln("%f, %f", time, m_turret.getCurrent());

        if (m_trg_flywheel.get()) {
            if (!m_debounce3) {
                m_target = SmartDashboard.getNumber("Target Velocity", 0);
                m_turret.setVelocity(m_target);
                m_turret.startFeeder();

                m_log_events.writeln("%f, TURRET STARTED [%f], %f", time, m_target, m_target);
            }

            m_debounce3 = true;
        } else {
            if (m_debounce3) {
                m_turret.setVelocity(0);
                m_turret.stopFeeder();

                m_log_events.writeln("%f, TURRET STOPPED [], ", time);
            }
                
            m_debounce3 = false;
        }

        if (m_trg_score.get()) {
            if (!m_debounce4)
                m_log_scored.writeln("%f, %f, %f, %f", time, m_distance, m_target, velocity);

            m_debounce4 = true;
        } else
            m_debounce4 = false;

        SmartDashboard.putNumber(      "Real Velocity", velocity);
        SmartDashboard.putNumber(    "Target Velocity", m_target);
        SmartDashboard.putNumber(    "Actual Velocity", m_turret.getVelocity());     
        SmartDashboard.putNumber( "Predicted Velocity", m_rpm_curve.calculate(m_distance));
        SmartDashboard.putNumber("Robot Distance (ft)", m_distance);
    }

    @Override
    public void end(boolean interrupted) {
        m_limelight.disable();
        m_turret.disable();

        m_indexer.moveIndexerMotor(0);

        m_log_velocity.save();
        m_log_current.save();
        m_log_scored.save();
        m_log_events.save();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}