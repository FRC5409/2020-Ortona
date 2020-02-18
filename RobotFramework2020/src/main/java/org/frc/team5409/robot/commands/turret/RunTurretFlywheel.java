package frc.robot.commands.turret;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.turret.TurretFlywheel;
import frc.robot.util.Range;

/**
 * Turns turret to {@code target} angle.
 * 
 * @author Keith Davies
 */
public final class RunTurretFlywheel extends CommandBase {
    private final TurretFlywheel m_turret;
    private final Indexer m_indexer;
    private boolean m_reached;

//=========================================
// TESTING
    private double m_target,
                   m_distance,
                   m_timer;

    private boolean m_debounce,
                    m_debounce2;

    private JoystickButton but_X1,
                        but_X2;

    private FileWriter m_log_current,
                       m_log_velocity,
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
    public RunTurretFlywheel(TurretFlywheel subsystem, Indexer sys, XboxController control1, XboxController control2) {
        m_turret = subsystem;
        m_indexer = sys;

        SmartDashboard.setDefaultNumber("Target Velocity", 0);
        SmartDashboard.setDefaultNumber("Target Indexer", 0);
        SmartDashboard.setDefaultNumber("Feeder Threshold", 0);
        SmartDashboard.setDefaultNumber("RPM Scale", 0);

        m_reached = false;
        m_debounce = false;
        m_debounce2 = false;
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

        m_target = SmartDashboard.getNumber("Target Velocity", 0);

//=========================================
// TESTING
        try {
            File logs = new File("/home/lvuser/flywheel_logs/" + Long.toString(Instant.now().getEpochSecond()) + "/");
            if (!logs.exists())
                logs.mkdirs();

            File log_current = new File(logs.getPath() + "/CURRENT_DRAW.csv");
            log_current.createNewFile();
            m_log_current = new FileWriter(log_current);

            File log_scored = new File(logs.getPath() + "/CELLS_SCORED.csv");
            log_scored.createNewFile();
            m_log_scored = new FileWriter(log_scored);

            File log_velocity = new File(logs.getPath() + "/FLYWHEEL_VELOCITY.csv");
            log_velocity.createNewFile();
            m_log_velocity = new FileWriter(log_velocity);
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
        double velocity = m_turret.getVelocity();
        if (velocity > m_target * Range.scalar(SmartDashboard.getNumber("Feeder Threshold", 1)) && !m_reached) {
            m_indexer.moveIndexerMotor(SmartDashboard.getNumber("Target Indexer", 0));
            m_reached = true;
        }
        double time = Timer.getFPGATimestamp() - m_timer;

//=========================================
// TESTING
        if (but_X1.get() && !m_debounce2) {
            m_turret.setVelocity(m_target);
            m_debounce2 = true;
        } else {
            m_turret.setVelocity(0);
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

        try {
            m_log_current.close();
            m_log_scored.close();
            m_log_velocity.close();
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