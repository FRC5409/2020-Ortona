package org.frc.team5409.robot.subsystems.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.*;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * Controls the Turret Flywheel.
 * 
 * @author Keith Davies
 */
public final class TurretFlywheel extends SubsystemBase implements Toggleable {
    private       CANSparkMax      mot_C00_turret_flywheel;
    private       CANSparkMax      mot_C01_turret_flywheel;
    private       CANSparkMax      mot_C02_turret_feeder;

    private       CANEncoder       enc_C00_turret_flywheel;
    private       CANPIDController pid_C00_turret_flywheel;

    private final Range            m_velocity_range;

    private       double           m_target;
    private       boolean          m_enabled, m_safety_enabled;
    private       Watchdog         m_watchdog;

    /**
     * Constructs a Turret Flywheel subsystem.
     */
    public TurretFlywheel() {
        mot_C00_turret_flywheel = new CANSparkMax(2, CANSparkMax.MotorType.kBrushless);
            mot_C00_turret_flywheel.restoreFactoryDefaults(true);
                mot_C00_turret_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C00_turret_flywheel.setInverted(true);
                mot_C00_turret_flywheel.setSmartCurrentLimit(Constants.TurretControl.turret_flywheel_current_limit);
            mot_C00_turret_flywheel.burnFlash();
 
        mot_C01_turret_flywheel = new CANSparkMax(10, CANSparkMax.MotorType.kBrushless);
            mot_C01_turret_flywheel.restoreFactoryDefaults(true);
                mot_C01_turret_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C01_turret_flywheel.follow(mot_C00_turret_flywheel, true);
            mot_C01_turret_flywheel.burnFlash();

        mot_C02_turret_feeder = new CANSparkMax(12, CANSparkMax.MotorType.kBrushless);
            mot_C02_turret_feeder.restoreFactoryDefaults(true);
                mot_C02_turret_feeder.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C02_turret_feeder.setSmartCurrentLimit(Constants.TurretControl.turret_feeder_current_limit);
            mot_C02_turret_feeder.burnFlash();
    
        enc_C00_turret_flywheel = mot_C00_turret_flywheel.getEncoder();
            enc_C00_turret_flywheel.setVelocityConversionFactor(Constants.TurretControl.turret_flywheel_rpm_scale);

        pid_C00_turret_flywheel = mot_C00_turret_flywheel.getPIDController();
            pid_C00_turret_flywheel.setFeedbackDevice(enc_C00_turret_flywheel);
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
     * Checks whether or not the turret flywheel 
     * subsystem is currently enabled.
     * 
     * @return The subsystems enabled state.
     */
    public boolean isEnabled() {
        return m_enabled;
    }

    /**
     * Sets the velocity of the turret flywheel.
     * 
     * @param target The target velocity.
     */
    public void setVelocity(double target) {
        if (!m_enabled)
            return;
        
        m_target = m_velocity_range.clamp(target);

        pid_C00_turret_flywheel.setReference(
            m_target * Constants.TurretControl.turret_flywheel_rpm_scale,
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

        return enc_C00_turret_flywheel.getVelocity();
    }

    /**
     * Checks if the turret flywheel velocity
     * has reached it's target setpoint.
     * 
     * @return If the flywheel velocity is at it's setpoint.
     */
    public boolean isTargetReached() {
        m_watchdog.feed();

        return (getVelocity() > m_target * Constants.TurretControl.turret_flywheel_target_thresh);
    }

    /**
     * Get's the current draw of the turret flywheel.
     */
    public double getCurrent() {
        return mot_C00_turret_flywheel.getOutputCurrent();
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
     * @param speed The feeder motor speed.
     */
    public void setFeederSpeed(double speed) {
        if (!m_enabled)
            return;

        mot_C02_turret_feeder.set(Range.scalar(speed));
        
        m_watchdog.feed();
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