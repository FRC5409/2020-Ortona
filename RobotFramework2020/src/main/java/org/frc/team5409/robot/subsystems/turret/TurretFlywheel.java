package frc.robot.subsystems.turret;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.*;

import org.frc.team5409.robot.Range;
import org.frc.team5409.robot.Watchdog;
import frc.robot.Constants;

/**
 * Controls the Turret Flywheel.
 * 
 * @author Keith Davies
 */
public final class TurretFlywheel extends SubsystemBase {
    private CANSparkMax      mot_C00_turret_flywheel;
    private CANSparkMax      mot_C01_turret_flywheel;
    private CANSparkMax      mot_C02_turret_feeder;

    private CANEncoder       enc_C01_turret_flywheel;
    private CANPIDController pid_C00_turret_flywheel;

    private final Range      m_velocity_range;

    private boolean          m_enabled;
    private boolean          m_safety_enabled;
    private Watchdog         m_watchdog;

    /**
     * Constructs a Turret Flywheel subsystem.
     */
    public TurretFlywheel() {
        mot_C00_turret_flywheel = new CANSparkMax(2, CANSparkMax.MotorType.kBrushless);
            mot_C00_turret_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
            mot_C00_turret_flywheel.setInverted(true);
            mot_C00_turret_flywheel.setSmartCurrentLimit(Constants.TurretControl.turret_flywheel_current_limit);

        mot_C00_turret_flywheel.burnFlash();
 
        mot_C01_turret_flywheel = new CANSparkMax(10, CANSparkMax.MotorType.kBrushless);
            mot_C01_turret_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
            mot_C01_turret_flywheel.follow(mot_C00_turret_flywheel, true);

        mot_C01_turret_flywheel.burnFlash();

        mot_C02_turret_feeder = new CANSparkMax(12, CANSparkMax.MotorType.kBrushless);
            mot_C02_turret_feeder.setIdleMode(CANSparkMax.IdleMode.kCoast);
            mot_C02_turret_feeder.setSmartCurrentLimit(Constants.TurretControl.turret_feeder_current_limit);
    
        enc_C01_turret_flywheel = mot_C00_turret_flywheel.getEncoder();
            // enc_C01_turret_flywheel.setVelocityConversionFactor((kconstants.turret_flywheel_diameter*Math.PI) / enc_C01_turret_flywheel.getCountsPerRevolution());

        pid_C00_turret_flywheel = mot_C00_turret_flywheel.getPIDController();
            pid_C00_turret_flywheel.setFeedbackDevice(enc_C01_turret_flywheel);
            pid_C00_turret_flywheel.setOutputRange(0, 1);
            pid_C00_turret_flywheel.setP(Constants.TurretControl.pid_turret_flywheel.P);
            pid_C00_turret_flywheel.setI(Constants.TurretControl.pid_turret_flywheel.I);
            pid_C00_turret_flywheel.setD(Constants.TurretControl.pid_turret_flywheel.D);

        m_velocity_range = Constants.TurretControl.turret_velocity_range;
        
        m_enabled = false;
        m_safety_enabled = true;
        m_watchdog = new Watchdog(Constants.TurretControl.turret_watchdog_expire_time);
    }
    
    /**
     * Enables the turret flywheel
     */
    public void enable() {
        if (m_enabled)
            return;

        m_enabled = true;
        m_watchdog.feed();
    }

    /**
     * Disables the turret flywheel.
     */
    public void disable() {
        if (!m_enabled)
            return;
        
        mot_C00_turret_flywheel.disable();
        mot_C02_turret_feeder.disable();

        m_enabled = false;
    }

    /**
     * Sets the velocity of the turret flywheel.
     * 
     * @param target The target velocity.
     */
    public void setVelocity(double target) {
        if (!m_enabled)
            return;

        pid_C00_turret_flywheel.setReference(
            m_velocity_range.clamp(target) * SmartDashboard.getNumber("RPM Scale", 4.45),
            ControlType.kVelocity
        );
       
        m_watchdog.feed();
    }

    /**
     * Gets the velocity of the turret flywheel.
     * 
     * @return The flywheel velocity.
     */
    public double getVelocity() {
        m_watchdog.feed();

        return enc_C01_turret_flywheel.getVelocity();
    }

    /**
     * Sets the raw output of the turret flywheel
     * motor. [0,1]
     * 
     * @param target The target output.
     */
    public void setRaw(double target) {
        if (!m_enabled)
            return;

        mot_C00_turret_flywheel.set(Range.scalar(target));
        
        m_watchdog.feed();
    }

    /**
     * Runs the turret feeder at {@code speed}.
     * 
     * @param speed The intake motor speed.
     */
    public void setFeederSpeed(double speed) {
        if (!m_enabled)
            return;

        mot_C02_turret_feeder.set(Range.scalar(speed));
        
        m_watchdog.feed();
    }

    public double getCurrent() {
        return mot_C00_turret_flywheel.getOutputCurrent();
    }

    /**
     * Runs the turret feeder at full speed.
     */
    public void startFeeder() {
        setFeederSpeed(1);
    }

    /**
     * Stops the turret feeder.
     */
    public void stopFeeder() {
        setFeederSpeed(0);
    }

    /**
     * Enables / disables turret safety. This
     * changes the impact of watchdog.
     * 
     * @param state The safety state.
     */
    public void setSafety(boolean state) {
        if (state)
            m_watchdog.feed();

        m_safety_enabled = state;
    }
    
    @Override
    public void periodic() {
        if (m_enabled && m_safety_enabled) {
            m_watchdog.update();
            if (m_watchdog.isExpired()) {
                disable();
            }
        }
    }
}