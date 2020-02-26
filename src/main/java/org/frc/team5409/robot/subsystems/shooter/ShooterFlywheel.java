package org.frc.team5409.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.*;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * Controls and operators the Shooter Flywheel.
 * 
 * @author Keith Davies
 */
public final class ShooterFlywheel extends SubsystemBase implements Toggleable {
    private       CANSparkMax      mot_C00_shooter_flywheel;
    private       CANSparkMax      mot_C01_shooter_flywheel;
    private       CANSparkMax      mot_C02_shooter_feeder;

    private       CANEncoder       enc_C00_shooter_flywheel;
    private       CANPIDController pid_C00_shooter_flywheel;

    private final Range            m_velocity_range;

    private       double           m_target;
    private       boolean          m_enabled, m_safety_enabled;
    private       Watchdog         m_watchdog;

    /**
     * Constructs a Turret Flywheel subsystem.
     */
    public ShooterFlywheel() {
        mot_C00_shooter_flywheel = new CANSparkMax(2, CANSparkMax.MotorType.kBrushless);
            mot_C00_shooter_flywheel.restoreFactoryDefaults(true);
                mot_C00_shooter_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C00_shooter_flywheel.setInverted(true);
                mot_C00_shooter_flywheel.setSmartCurrentLimit(Constants.ShooterControl.shooter_flywheel_current_limit);
            mot_C00_shooter_flywheel.burnFlash();
 
        mot_C01_shooter_flywheel = new CANSparkMax(10, CANSparkMax.MotorType.kBrushless);
            mot_C01_shooter_flywheel.restoreFactoryDefaults(true);
                mot_C01_shooter_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C01_shooter_flywheel.follow(mot_C00_shooter_flywheel, true);
            mot_C01_shooter_flywheel.burnFlash();

        mot_C02_shooter_feeder = new CANSparkMax(12, CANSparkMax.MotorType.kBrushless);
            mot_C02_shooter_feeder.restoreFactoryDefaults(true);
                mot_C02_shooter_feeder.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C02_shooter_feeder.setSmartCurrentLimit(Constants.ShooterControl.shooter_feeder_current_limit);
            mot_C02_shooter_feeder.burnFlash();
    
        enc_C00_shooter_flywheel = mot_C00_shooter_flywheel.getEncoder();
            enc_C00_shooter_flywheel.setVelocityConversionFactor(Constants.ShooterControl.shooter_flywheel_rpm_scale);

        pid_C00_shooter_flywheel = mot_C00_shooter_flywheel.getPIDController();
            pid_C00_shooter_flywheel.setFeedbackDevice(enc_C00_shooter_flywheel);
            pid_C00_shooter_flywheel.setOutputRange(0, 1);
            pid_C00_shooter_flywheel.setP(Constants.ShooterControl.shooter_flywheel_pid.P);
            pid_C00_shooter_flywheel.setI(Constants.ShooterControl.shooter_flywheel_pid.I);
            pid_C00_shooter_flywheel.setD(Constants.ShooterControl.shooter_flywheel_pid.D);

        m_velocity_range = Constants.ShooterControl.shooter_velocity_range;
        
        m_enabled = false;
        m_safety_enabled = true;

        m_watchdog = new Watchdog(Constants.ShooterControl.shooter_watchdog_expire_time);
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
        
        mot_C00_shooter_flywheel.disable();
        mot_C02_shooter_feeder.disable();

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

        pid_C00_shooter_flywheel.setReference(
            m_target * Constants.ShooterControl.shooter_flywheel_rpm_scale,
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

        return enc_C00_shooter_flywheel.getVelocity();
    }

    /**
     * Checks if the turret flywheel velocity
     * has reached it's target setpoint.
     * 
     * @return If the flywheel velocity is at it's setpoint.
     */
    public boolean isTargetReached() {
        m_watchdog.feed();

        return (getVelocity() > m_target * Constants.ShooterControl.shooter_flywheel_target_thresh);
    }

    /**
     * Get's the current draw of the turret flywheel.
     */
    public double getCurrent() {
        return mot_C00_shooter_flywheel.getOutputCurrent();
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

        mot_C00_shooter_flywheel.set(Range.scalar(target));
        
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

        mot_C02_shooter_feeder.set(Range.scalar(speed));
        
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