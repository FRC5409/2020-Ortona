package org.frc.team5409.robot.commands.shooter.logging;

import java.time.Instant;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;

import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * This command runs the shooter flywheel at a speed
 * porportional to the distance of the turret from the
 * shooting target and operates the indexer once the rpm
 * reaches it's setpoint.
 * 
 * @author Keith Davies
 */
public final class RunShooterFlywheel extends CommandBase {
    private final ShooterFlywheel m_flywheel;
    private final Limelight       m_limelight;
    private final Indexer         m_indexer;
    
    private final double          m_target_height;

    private final Range           m_distance_range;
    private final SimpleEquation  m_rpm_curve;

    private       double          m_target, m_distance, m_timer, m_predicted, m_scored, m_missed;

    private       boolean         m_debounce1, m_debounce2, m_debounce3, m_debounce4;

    private       JoystickButton  m_trg_flywheel, m_trg_score, m_trg_miss;

    private       Logger          m_log_flywheel, m_log_events, m_log_powercells;

    public RunShooterFlywheel(ShooterFlywheel sys_flywheel, Indexer sys_indexer, Limelight sys_limelight, XboxController joy_main, XboxController joy_sec) {
        m_limelight = sys_limelight;
        m_flywheel = sys_flywheel;
        m_indexer = sys_indexer;

        m_predicted = 0;
        m_distance = 0;
        m_target = 0;
        m_timer = 0;

        m_trg_flywheel = new JoystickButton(joy_main, 1);
        m_trg_score = new JoystickButton(joy_sec, 1);
        m_trg_miss = new JoystickButton(joy_sec, 2);

        m_target_height = Math.abs(Constants.Vision.vision_outerport_height - Constants.Vision.vision_limelight_height);
        m_distance_range = Constants.ShooterControl.shooter_distance_range;
        m_rpm_curve = Constants.ShooterControl.shooter_distance_rpm_curve;

        addRequirements(m_flywheel, m_indexer, m_limelight);
    }

    @Override
    public void initialize() {
        m_flywheel.enable();
        
        if (!m_limelight.isEnabled()) {
            m_limelight.enable();
            m_limelight.setLedMode(Limelight.LedMode.kModeOn);
        }

        String logs_path = "flywheel/"+Long.toString(Instant.now().getEpochSecond());

        m_log_flywheel = new Logger(logs_path+"/FLYWHEEL_DATA.csv");
        m_log_powercells = new Logger(logs_path+"/POWERCELL_EVENTS.csv");
        m_log_events = new Logger(logs_path+"/TURRET_EVENTS.csv");

        m_debounce1 = false;
        m_debounce2 = false;
        m_debounce3 = false;
        m_debounce4 = false;

        m_predicted = 0;
        m_distance = 0;
        m_target = 0;
        m_timer = Timer.getFPGATimestamp();
        
        m_scored = 0;
        m_missed = 0;

        new Logger(logs_path+"/TURRET_CONSTANTS.csv")
            .write("%f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %s",
                Constants.ShooterControl.shooter_flywheel_pid.P,
                Constants.ShooterControl.shooter_flywheel_pid.I,
                Constants.ShooterControl.shooter_flywheel_pid.D,
                Constants.ShooterControl.shooter_flywheel_pid.F,

                Constants.ShooterControl.shooter_flywheel_current_limit,
                Constants.ShooterControl.shooter_feeder_current_limit,

                Constants.ShooterControl.shooter_flywheel_target_thresh,

                Constants.Vision.vision_limelight_height,
                Constants.Vision.vision_outerport_height,
                Constants.Vision.vision_limelight_pitch,

                Constants.ShooterControl.shooter_distance_rpm_curve_string)
            .save();
        
        m_log_events.writeln("0, SESSION START, ");
        m_log_flywheel.writeln("0, 0, 0, 0");
    }

    @Override
    public void execute() {
        double velocity = m_flywheel.getVelocity();
        double time = Timer.getFPGATimestamp() - m_timer;

        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            //m_distance = m_distance_range.clamp(m_target_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch)));
            m_distance = m_target_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch));
            m_predicted = m_rpm_curve.calculate(m_distance);

            if (!m_debounce1)
                m_log_events.writeln("%f, TARGET AQUIRED [%f], %f", time, m_distance, m_distance);

            m_debounce1 = true;
        } else {
            if (m_debounce1)
                m_log_events.writeln("%f, TARGET LOST [%f], %f", time, m_distance, m_distance);
            
            m_distance = -1;

            m_debounce1 = false;
        }

        if (velocity > m_target*Constants.ShooterControl.shooter_flywheel_target_thresh) {
            m_indexer.moveIndexerMotor(-0.8);

            if (!m_debounce2) {
                m_log_events.writeln("%f, FLYWHEEL AT SPEED [%f], %f", time, velocity, velocity);
                m_log_events.writeln("%f, INDEXER STARTED [0.8], 0.8", time);
            }
            
            m_debounce2 = true;
        } else {
            m_indexer.moveIndexerMotor(0);

            if (m_debounce2)
                m_log_events.writeln(String.format("%f, INDEXER STOPPED [0], 0", time));

            m_debounce2 = false;
        }

        if (m_trg_flywheel.get()) {
            if (!m_debounce3) {
                m_target = SmartDashboard.getNumber("Target Velocity", 0);
                m_flywheel.setVelocity(m_target);
                m_flywheel.startFeeder();

                m_scored = 0;
                m_missed = 0;

                m_log_events.writeln("%f, FLYWHEEL STARTED [%f], %f", time, m_target, m_target);
            }

            m_debounce3 = true;
        } else {
            if (m_debounce3) {
                m_flywheel.setVelocity(0);
                m_flywheel.stopFeeder();

                m_log_events.writeln("%f, FLYWHEEL STOPPED [0], 0", time);
            }
                
            m_debounce3 = false;
        }

        if (m_trg_score.get() || m_trg_miss.get()) {
            if (!m_debounce4) {
                if (m_trg_score.get()) {
                    m_log_events.writeln("%f, POWERCELL SCORED [%f], %f", time, m_distance, m_distance);
                    m_log_powercells.writeln("1, %f, %f, %f, %f, %f", time, m_distance, m_target, m_predicted, velocity);

                    m_scored++;
                } else if (m_trg_miss.get()) {
                    m_log_events.writeln("%f, POWERCELL MISSED [%f], %f", time, m_distance, m_distance);
                    m_log_powercells.writeln("0, %f, %f, %f, %f, %f", time, m_distance, m_target, m_predicted, velocity);

                    m_missed++;
                }
            }

            m_debounce4 = true;
        } else
            m_debounce4 = false;

        m_log_flywheel.writeln("%f, %f, %f, %f", time, 
                                                 velocity,
                                                 m_flywheel.getData(ShooterFlywheel.ShooterData.kFlywheelCurrent),
                                                 m_flywheel.getData(ShooterFlywheel.ShooterData.kFeederCurrent)
                              );

        SmartDashboard.putNumber(      "Real Velocity", velocity);
        SmartDashboard.putNumber(    "Target Velocity", m_target);
        SmartDashboard.putNumber(    "Actual Velocity", m_flywheel.getVelocity());     
        SmartDashboard.putNumber( "Predicted Velocity", m_predicted);
        SmartDashboard.putNumber("Robot Distance (ft)", m_distance);
        SmartDashboard.putNumber(  "Scored Powercells", m_scored);
        SmartDashboard.putNumber(  "Missed Powercells", m_missed);
    }

    @Override
    public void end(boolean interrupted) {
        double time = Timer.getFPGATimestamp() - m_timer;

        m_limelight.disable();
        m_flywheel.disable();

        m_indexer.moveIndexerMotor(0);

        if (m_debounce3)
            m_log_events.writeln("%f, FLYWHEEL STOPPED [0], 0", time);

        if (m_debounce2)
            m_log_events.writeln(String.format("%f, INDEXER STOPPED [0], 0", time));

        if (m_debounce1)
            m_log_events.writeln("%f, TARGET LOST [%f], %f", time, m_distance, m_distance);

        m_log_events.writeln("%f, SESSION END, ", time);

        m_log_flywheel.save();
        m_log_powercells.save();
        m_log_events.save();
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}