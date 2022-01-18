package org.frc.team5409.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

import com.revrobotics.*;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * Controls and operates the Shooter Turret.
 * 
 * @author Keith Davies
 */
public final class ShooterTurret extends SubsystemBase implements Toggleable {
    /**
     * Represents the reset (limit) switches along the shooter 
     * turrets rotation as well as their respective angles.
     */
    public enum ResetSwitchType {
        kLeft(Constants.ShooterControl.shooter_turret_limit_left_angle),
        
        kCenter(Constants.ShooterControl.shooter_turret_limit_center_angle),

        kRight(Constants.ShooterControl.shooter_turret_limit_right_angle),

        kNone(-1);

        ResetSwitchType(double angle) {
            this.angle = angle;
        }

        public final double angle;
    }

    private CANSparkMax           mot_C05_shooter_turret;
    
    private SparkMaxLimitSwitch   dio_C05_turret_limit_left,
                                  dio_C05_turret_limit_right;
    
    private DigitalInput          dio_i01_turret_limit_center;

    private RelativeEncoder       enc_C05_shooter_turret;
    private SparkMaxPIDController pid_C05_shooter_turret;
                             
    private final double          m_target_reached_thresh;
    private final Range           m_turret_range;

    private double                m_target;

    private boolean               m_enabled;
    private boolean               m_safety_enabled;
    private Watchdog              m_watchdog;

    /**
     * Constructs a Shooter Turret subsystem.
     */
    public ShooterTurret() {
        mot_C05_shooter_turret = new CANSparkMax(5, CANSparkMax.MotorType.kBrushless);
        mot_C05_shooter_turret.restoreFactoryDefaults();
            mot_C05_shooter_turret.setIdleMode(CANSparkMax.IdleMode.kBrake);
            mot_C05_shooter_turret.setSmartCurrentLimit(Constants.ShooterControl.shooter_turret_current_limit);
        mot_C05_shooter_turret.burnFlash();

        // dio_C05_turret_limit_left = mot_C05_shooter_turret.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyOpen);
        dio_C05_turret_limit_left = mot_C05_shooter_turret.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
            dio_C05_turret_limit_left.enableLimitSwitch(true);
            
        dio_C05_turret_limit_right = mot_C05_shooter_turret.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
            dio_C05_turret_limit_right.enableLimitSwitch(true);

        dio_i01_turret_limit_center = new DigitalInput(1);

        enc_C05_shooter_turret = mot_C05_shooter_turret.getEncoder();
            enc_C05_shooter_turret.setPositionConversionFactor(7d);

        pid_C05_shooter_turret = mot_C05_shooter_turret.getPIDController();
            pid_C05_shooter_turret.setFeedbackDevice(enc_C05_shooter_turret);
            pid_C05_shooter_turret.setOutputRange(
                -Constants.ShooterControl.shooter_turret_max_speed,
                 Constants.ShooterControl.shooter_turret_max_speed
            );
            pid_C05_shooter_turret.setP(Constants.ShooterControl.shooter_turret_pid.P);
            pid_C05_shooter_turret.setI(Constants.ShooterControl.shooter_turret_pid.I);
            pid_C05_shooter_turret.setD(Constants.ShooterControl.shooter_turret_pid.D);
 
        m_target_reached_thresh = Constants.ShooterControl.shooter_turret_target_thresh;
        m_turret_range = Constants.ShooterControl.shooter_turret_range;

        m_target = 0;
        
        m_enabled = false;
        m_safety_enabled = true;

        m_watchdog = new Watchdog(Constants.ShooterControl.shooter_watchdog_expire_time);

        /*var parent = Shuffleboard.getTab("Robot Information").getLayout("Shooter Information", BuiltInLayouts.kList);
            parent.addNumber("Turret Angle", this::getRotation);
            var child = parent.getLayout("Shooter Target Information", BuiltInLayouts.kGrid);
                child.addNumber("Rotation Target", () -> { return m_target; });
                child.addBoolean("Rot. Reached", this::isTargetReached);*/
    }
 
    /**
     * Enables the Shooter Turret.
     */
    public void enable() {
        if (m_enabled)
            return;

        m_enabled = true;
        m_watchdog.feed();
    }

    /**
     * Disables the Shooter Turret.
     */
    public void disable() {
        if (!m_enabled)
            return;

        mot_C05_shooter_turret.disable();
        m_enabled = false;
    }

    /**
     * Checks whether or not the shooter turret 
     * subsystem is currently enabled.
     * 
     * @return The subsystems enabled state.
     */
    public boolean isEnabled() {
        return m_enabled;
    }

    /**
     * Sets the rotation of the turret.
     * 
     * @param target The target angle.
     */
    public void setRotation(double target) {
        if (!m_enabled)
            return;

        m_target = m_turret_range.clamp(target);
        pid_C05_shooter_turret.setReference(target, CANSparkMax.ControlType.kPosition);
        
        m_watchdog.feed();
    }
    
    /**
     * Get's the rotation of the turret.
     * 
     * @return The turret rotation.
     */
    public double getRotation() {
        m_watchdog.feed();

        return enc_C05_shooter_turret.getPosition();
    }

    /**
     * Checks if the turret's rotation is at
     * it's target setpoint.
     * 
     * @return If the turret rotation is at it's setpoint
     */
    public boolean isTargetReached() {
        m_watchdog.feed();

        return (Math.abs(getRotation()-m_target) < m_target_reached_thresh);
    }

    /**
     * Sets the raw output of the turret rotation
     * motor. [-1,1]
     * 
     * @param target The target output.
     */
    public void setRaw(double target) {
        if (!m_enabled)
            return;

        mot_C05_shooter_turret.set(Range.clamp(0, target, 0.05));

        m_watchdog.feed();
    }

    /**
     * Enables / disables turret safety. 
     * 
     * <p> This changes the impact that watchdog
     * has on the subsystem </p>
     * 
     * @param state The safety state.
     */
    public void setSafety(boolean state) {
        if (state)
            m_watchdog.feed();

        m_safety_enabled = state;
    }

    /**
     * Resets the turret rotation encoder
     * to the specified reset switch angle. 
     * 
     * @param type The active reset switch.
     */
    public void resetRotation(ResetSwitchType type) {
        if (type != ResetSwitchType.kNone) {
            m_target = m_turret_range.clamp(type.angle);
            enc_C05_shooter_turret.setPosition(m_target);
        }
    }

    public double getTarget() {
        return m_target;
    }

    /**
     * Get's the currently active reset switch.
     * 
     * @return The active reset switch.
     */
    public ResetSwitchType getActiveResetSwitch() {
        m_watchdog.feed();

        if (dio_C05_turret_limit_left.isPressed())
            return ResetSwitchType.kLeft;

        if (!dio_i01_turret_limit_center.get())
            return ResetSwitchType.kCenter;
        
        if (dio_C05_turret_limit_right.isPressed())
            return ResetSwitchType.kRight;
        
        return ResetSwitchType.kNone;
    }
    
    @Override
    public void periodic() {
        if (m_enabled && m_safety_enabled) {
            m_watchdog.update();
            if (m_watchdog.isExpired())
                disable();
        }
    }
}