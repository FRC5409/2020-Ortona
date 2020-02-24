package org.frc.team5409.robot.subsystems.turret;

import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;
import com.revrobotics.*;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.util.*;

/**
 * Controls Turret Rotation.
 * 
 * @author Keith Davies
 */
public final class TurretRotation extends SubsystemBase implements Toggleable {
    /**
     * Represents the reset (limit) switches along the turret 
     * rotation as well as their respective angles.
     */
    public enum ResetSwitchType {
        kLeft(Constants.TurretControl.turret_rotation_limit_left_angle),
        kCenter(Constants.TurretControl.turret_rotation_limit_center_angle),
        kRight(Constants.TurretControl.turret_rotation_limit_right_angle),
        kNone(-1);

        ResetSwitchType(double angle) {
            this.angle = angle;
        }

        public final double angle;
    }

    private CANSparkMax      mot_C00_turret_rotation;
    
    private CANDigitalInput  dio_C00_turret_limit_left,
                             dio_C00_turret_limit_right;
    
    private DigitalInput     dio_i00_turret_limit_center;

    private CANEncoder       enc_C01_turret_rotation;
    private CANPIDController pid_C00_turret_rotation;
                             
    private final double     m_target_reached_thresh;
    private final Range      m_rotation_range;

    private double           m_rotation;

    private boolean          m_enabled;
    private boolean          m_safety_enabled;
    private Watchdog         m_watchdog;

    /**
     * Constructs a Turret Rotation subsystem.
     */
    public TurretRotation() {
        mot_C00_turret_rotation = new CANSparkMax(0, CANSparkMax.MotorType.kBrushless);
            mot_C00_turret_rotation.setIdleMode(CANSparkMax.IdleMode.kBrake);
            mot_C00_turret_rotation.setSmartCurrentLimit(Constants.TurretControl.turret_rotation_current_limit);
            
        SendableRegistry.addChild(this, mot_C00_turret_rotation);

        // TODO determine limit switch polarity and orientation.
        dio_C00_turret_limit_left = mot_C00_turret_rotation.getForwardLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            dio_C00_turret_limit_left.enableLimitSwitch(true);
            
        SendableRegistry.addChild(this, dio_C00_turret_limit_left);
            
        dio_C00_turret_limit_right = mot_C00_turret_rotation.getReverseLimitSwitch(CANDigitalInput.LimitSwitchPolarity.kNormallyClosed);
            dio_C00_turret_limit_right.enableLimitSwitch(true);
            
        SendableRegistry.addChild(this, dio_C00_turret_limit_right);

        dio_i00_turret_limit_center = new DigitalInput(0);
            
        SendableRegistry.addChild(this, dio_i00_turret_limit_center);

        enc_C01_turret_rotation = mot_C00_turret_rotation.getEncoder();
            enc_C01_turret_rotation.setPositionConversionFactor(360 / enc_C01_turret_rotation.getCountsPerRevolution());

        pid_C00_turret_rotation = mot_C00_turret_rotation.getPIDController();
            pid_C00_turret_rotation.setFeedbackDevice(enc_C01_turret_rotation);
            pid_C00_turret_rotation.setOutputRange(-1, 1);
            pid_C00_turret_rotation.setP(Constants.TurretControl.pid_turret_rotation.P);
            pid_C00_turret_rotation.setI(Constants.TurretControl.pid_turret_rotation.I);
            pid_C00_turret_rotation.setD(Constants.TurretControl.pid_turret_rotation.D);

        SendableRegistry.addChild(this, pid_C00_turret_rotation);

        m_target_reached_thresh = Constants.TurretControl.turret_rotation_target_thresh;
        m_rotation_range = Constants.TurretControl.turret_rotation_range;

        m_rotation = 0;

        m_enabled = false;
        m_safety_enabled = true;
        m_watchdog = new Watchdog(Constants.TurretControl.turret_watchdog_expire_time);

        mot_C00_turret_rotation.burnFlash();
    }
 
    /**
     * Enables the Turret Rotation.
     */
    public void enable() {
        if (m_enabled)
            return;

        m_enabled = true;
        m_watchdog.feed();
    }

    /**
     * Disables the Turret Rotation.
     */
    public void disable() {
        if (!m_enabled)
            return;

        mot_C00_turret_rotation.disable();
        m_enabled = false;
    }

    /**
     * Checks whether or not the turret rotation 
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

        m_rotation = m_rotation_range.clamp(target);
        pid_C00_turret_rotation.setReference(target, ControlType.kPosition);

        m_watchdog.feed();
    }
    
    /**
     * Get's the rotation of the turret.
     * 
     * @return The turret rotation.
     */
    public double getRotation() {
        m_watchdog.feed();

        return enc_C01_turret_rotation.getPosition();
    }

    /**
     * Checks if the turret's rotation is within
     * the {@code target} angle range.
     * 
     * @param target The target rotation
     * 
     * @return If the turret is within the target range.
     */
    public boolean isAt(double target) {
        m_watchdog.feed();

        return (Math.abs(getRotation()-target) < m_target_reached_thresh);
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

        mot_C00_turret_rotation.set(Range.normalize(target));

        m_watchdog.feed();
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

    /**
     * Resets the rotation to he
     * specified reset switch angle. 
     * 
     * @param type The active reset switch.
     */
    public void resetRotation(ResetSwitchType type) {
        m_rotation = m_rotation_range.clamp(type.angle);
        enc_C01_turret_rotation.setPosition(m_rotation);
    }

    /**
     * Get's the currently active reset switch.
     * 
     * @return The active reset switch.
     */
    public ResetSwitchType getActiveResetSwitch() {
        if (dio_C00_turret_limit_left.isLimitSwitchEnabled())
            return ResetSwitchType.kLeft;

        if (dio_i00_turret_limit_center.get())
            return ResetSwitchType.kCenter;
        
        if (dio_C00_turret_limit_right.isLimitSwitchEnabled())
            return ResetSwitchType.kRight;
        
        return ResetSwitchType.kNone;
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
