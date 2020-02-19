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
import org.frc.team5409.robot.subsystems.Indexer;
import org.frc.team5409.robot.subsystems.turret.TurretFlywheel;
import org.frc.team5409.robot.subsystems.Limelight;
import org.frc.team5409.robot.util.Range;
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
    private double m_target,
                   m_distance,
                   m_timer;

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

        addRequirements(m_turret, m_indexer);
    }

    @Override
    public void initialize() {
        m_turret.enable();
        m_limelight.enable();
        m_limelight.setLedMode(Limelight.LedMode.kModeOn);

        m_target = SmartDashboard.getNumber("Target Velocity", 0);

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

        m_turret.startFeeder();
        System.out.println("TURRET RAN");
    }

    @Override
    public void execute() {
        double time = Timer.getFPGATimestamp() - m_timer;
        if (m_limelight.hasTarget()) {
            Vec2 target = m_limelight.getTarget();

            m_distance = Math.abs(SmartDashboard.getNumber("Target Height", 0) - SmartDashboard.getNumber("Limelight Height", 0)) / 
                Math.tan(target.y+SmartDashboard.getNumber("Limelight Angle", 0));

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

        if (velocity > m_target * Range.scalar(SmartDashboard.getNumber("Feeder Threshold", 100)) && !m_reached) {
            m_indexer.moveIndexerMotor(SmartDashboard.getNumber("Target Indexer", 0));
            m_reached = true;
        }

//=========================================
// TESTING
        if (but_X1.get() && !m_debounce2) {
            m_turret.setVelocity(m_target);

            try {
                m_log_events.write(String.format("%f, TURRET STARTED [%f]\n", time, m_target));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            m_debounce2 = true;
        } else {
            m_turret.setVelocity(0);

            if (m_debounce2)
                try {
                    m_log_events.write(String.format("%f, TURRET STOPPED [%f]\n", time, m_target));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            m_debounce2 = false;
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