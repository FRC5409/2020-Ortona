package org.frc.team5409.robot.commands.turret;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.Indexer;
import org.frc.team5409.robot.subsystems.turret.TurretFlywheel;
import org.frc.team5409.robot.subsystems.Limelight;
import org.frc.team5409.robot.util.Range;
import org.frc.team5409.robot.util.SimpleEquation;
import org.frc.team5409.robot.util.Vec2;

/**
 * Turns turret to {@code target} angle.
 * 
 * @author Keith Davies
 */
public final class RunTurretFlywheel extends CommandBase {
    private final TurretFlywheel m_turret;
    private final Indexer m_indexer;
    private final Limelight m_limelight;
    private boolean m_reached;

//=========================================
// TESTING
    
private final double         m_target_height;
private       double         m_target,
                             m_distance, m_timer;

private final Range          m_distance_range;
private final SimpleEquation m_rpm_curve;

    private boolean m_debounce,
                    m_debounce2,
                    m_debounce3;

    private JoystickButton but_X1,
                        but_X2;

    private FileWriter m_log_current,
                       m_log_velocity,
                       m_log_events,
                       m_log_scored;
//=========================================

    /**
     * Constructs a Run Turret command.
     * 
     * @param subsystem The turret flywheel subsystem.
     * @param target    The target angle.
     * 
     * @author Keith Davies
     */
    public RunTurretFlywheel(TurretFlywheel subsystem, Indexer sys, Limelight ll, XboxController control1, XboxController control2) {
        m_turret = subsystem;
        m_indexer = sys;
        m_limelight = ll;

        SmartDashboard.setDefaultNumber("Target Velocity", 0);
        SmartDashboard.setDefaultNumber("Target Indexer", 0.8);
        SmartDashboard.setDefaultNumber("Feeder Threshold", 0.9);
        SmartDashboard.setDefaultNumber("RPM Scale", 4.25);
        SmartDashboard.setDefaultNumber("Limelight Height", 0);
        SmartDashboard.setDefaultNumber("Target Height", 0);
        SmartDashboard.setDefaultNumber("Limelight Angle", 0);

        m_reached = false;
        m_debounce = false;
        m_debounce2 = false;
        m_debounce3 = false;
        m_target = 0;
        m_timer = 0;
        m_distance = 0;
        but_X1 = new JoystickButton(control1, 1);
        but_X2 = new JoystickButton(control2, 1);

        m_target_height = Math.abs(Constants.Vision.vision_outerport_height - Constants.Vision.vision_limelight_height);

        m_distance_range = Constants.TurretControl.turret_distance_range;
        m_rpm_curve = Constants.TurretControl.turret_distance_rpm_curve;

        addRequirements(m_turret, m_indexer);
    }

    @Override
    public void initialize() {
        m_turret.enable();

        m_distance = 0;
        
        if (!m_limelight.isEnabled()) {
            m_limelight.enable();
            m_limelight.setLedMode(Limelight.LedMode.kModeOn);
        }

//=========================================
// TESTING
        try {
            File logs = new File("/home/lvuser/flywheel_logs/" + Long.toString(Instant.now().getEpochSecond()) + "/");
            if (!logs.exists())
                logs.mkdirs();

            File log_current = new File(logs.getPath() + "/TURRET_FLYWHEEL_CURRENT.csv");
            log_current.createNewFile();
            m_log_current = new FileWriter(log_current);

            File log_events = new File(logs.getPath() + "/TURRET_EVENTS.csv");
            log_events.createNewFile();
            m_log_events = new FileWriter(log_events);

            File log_velocity = new File(logs.getPath() + "/TURRET_FLYWHEEL_VELOCITY.csv");
            log_velocity.createNewFile();
            m_log_velocity = new FileWriter(log_velocity);

            File log_scored = new File(logs.getPath() + "/TURRET_POWERCELL_SCORES.csv");
            log_scored.createNewFile();
            m_log_scored = new FileWriter(log_scored);
        } catch (IOException e) {
            e.printStackTrace();
        }
//=========================================

        m_timer = Timer.getFPGATimestamp();
        System.out.println("TURRET RAN");
    }

    @Override
    public void execute() {

        m_target = SmartDashboard.getNumber("Target Velocity", 0);
        double time = Timer.getFPGATimestamp() - m_timer;
        if (m_limelight.hasTarget() && m_limelight.getTargetType() == Limelight.TargetType.kOuterPort) {
            Vec2 target = m_limelight.getTarget();

            m_distance = m_distance_range.clamp(m_target_height / Math.tan(Math.toRadians(target.y + Constants.Vision.vision_limelight_pitch)));
            SmartDashboard.putNumber("Predicted rpm", m_rpm_curve.calculate(m_distance));

            SmartDashboard.putNumber("Robot Distance (ft)", m_distance);

            if (!m_debounce3) {
                try {
                    m_log_events.write(String.format("%f, TARGET AQUIRED\n", time));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            m_debounce3 = true;
        } else {
            m_distance = -1;

            if (m_debounce3) {
                try {
                    m_log_events.write(String.format("%f, TARGET LOST\n", time));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            m_debounce3 = false;
        }

        double velocity = m_turret.getVelocity();

        SmartDashboard.putNumber("Target Velocity", m_target);
        SmartDashboard.putNumber("Actual Velocity", m_turret.getVelocity());
        SmartDashboard.putNumber("Distance (ft)", m_distance);

        if (m_turret.getVelocity() > m_target*Constants.TurretControl.turret_flywheel_target_thresh)
            m_indexer.moveIndexerMotor(-0.8);
        else
            m_indexer.moveIndexerMotor(0);

//=========================================
// TESTING
        if (but_X1.get()) {
            if (!m_debounce2) {
                m_turret.setVelocity(m_target);
                m_turret.startFeeder();

                try {
                    m_log_events.write(String.format("%f, TURRET STARTED [%f]\n", time, m_target));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                m_debounce2 = true;
            }
        } else {
            if (m_debounce2) {
                m_turret.setVelocity(0);
                m_turret.stopFeeder();

                try {
                    m_log_events.write(String.format("%f, TURRET STOPPED [%f]\n", time, m_target));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
                m_debounce2 = false;
            }
                
        }

        try {
            m_log_velocity.write(String.format("%f, %f\n", time, velocity));
            m_log_current.write(String.format("%f, %f\n", time, m_turret.getCurrent()));

            if (but_X2.get() && !m_debounce) {
                m_log_scored.write(String.format("%f, %f, %f, %f\n", time, m_distance, m_target, velocity));
                m_debounce = true;
            } else
                m_debounce = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
//=========================================

        SmartDashboard.putNumber("Real Velocity", velocity);
    }

    @Override
    public void end(boolean interrupted) {
        m_indexer.moveIndexerMotor(0);
        m_turret.disable();
        m_limelight.disable();

        try {
            m_log_current.close();
            m_log_events.close();
            m_log_velocity.close();
            m_log_scored.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        m_reached = false;
        System.out.println("TURRET STOPPED");
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}