package org.frc.team5409.robot.subsystems.shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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
    public enum ShooterData {
        kFlywheelVelocity, kFlywheelCurrent, kFeederCurrent, kFlywheelTarget, kFlywheelOffset
    }

    private enum ShooterEvent {
        kSubsystemEnabled(0x0), kSubsystemDisabled(0x1), kSafetyEnabled(0x2), kSafetyDisabled(0x3),
        kWatchdogExpired(0x4), kFlywheelStarted(0x5), kFlywheelTargetChanged(0x6),
        kFlywheelStopped(0x7), kFlywheelAtSpeed(0x8), kFeederStarted(0x9), kFeederStopped(0xA);

        private ShooterEvent(int id) {
            this.id = (byte) id;
        }

        public final byte id;
    }

    private       CANSparkMax      mot_C07_shooter_flywheel;
    private       CANSparkMax      mot_C19_shooter_flywheel;
    private       CANSparkMax      mot_C18_shooter_feeder;

    private       CANEncoder       enc_C07_shooter_flywheel;
    private       CANPIDController pid_C07_shooter_flywheel;

    private final Range            m_velocity_range;
    private       double           m_velocity_offset;

    private       double           m_target;
    private       boolean          m_enabled, m_safety_enabled;
    private       Watchdog         m_watchdog;

    private       RawLogger        m_logger;

    /**
     * Constructs a Turret Flywheel subsystem.
     */
    public ShooterFlywheel() {
        mot_C07_shooter_flywheel = new CANSparkMax(7, CANSparkMax.MotorType.kBrushless);
            mot_C07_shooter_flywheel.restoreFactoryDefaults();
            mot_C07_shooter_flywheel.setSmartCurrentLimit(Constants.ShooterControl.shooter_flywheel_current_limit);
            mot_C07_shooter_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
            mot_C07_shooter_flywheel.setInverted(true);
            mot_C07_shooter_flywheel.follow(CANSparkMax.ExternalFollower.kFollowerDisabled, 0);
 
        mot_C19_shooter_flywheel = new CANSparkMax(19, CANSparkMax.MotorType.kBrushless);
            mot_C19_shooter_flywheel.restoreFactoryDefaults();
            mot_C19_shooter_flywheel.setIdleMode(CANSparkMax.IdleMode.kCoast);
            mot_C19_shooter_flywheel.follow(mot_C07_shooter_flywheel, true);

        mot_C19_shooter_flywheel.burnFlash();
        mot_C07_shooter_flywheel.burnFlash();

        mot_C18_shooter_feeder = new CANSparkMax(18, CANSparkMax.MotorType.kBrushless);
            mot_C18_shooter_feeder.restoreFactoryDefaults(true);
                mot_C18_shooter_feeder.setIdleMode(CANSparkMax.IdleMode.kCoast);
                mot_C18_shooter_feeder.setSmartCurrentLimit(Constants.ShooterControl.shooter_feeder_current_limit);
            mot_C18_shooter_feeder.burnFlash();
    
        enc_C07_shooter_flywheel = mot_C07_shooter_flywheel.getEncoder();
            enc_C07_shooter_flywheel.setVelocityConversionFactor(1);//Constants.ShooterControl.shooter_flywheel_rpm_scale);

        pid_C07_shooter_flywheel = mot_C07_shooter_flywheel.getPIDController();
            pid_C07_shooter_flywheel.setFeedbackDevice(enc_C07_shooter_flywheel);
            pid_C07_shooter_flywheel.setOutputRange(0, 1);
            pid_C07_shooter_flywheel.setP(Constants.ShooterControl.shooter_flywheel_pid.P);
            pid_C07_shooter_flywheel.setI(Constants.ShooterControl.shooter_flywheel_pid.I);
            pid_C07_shooter_flywheel.setD(Constants.ShooterControl.shooter_flywheel_pid.D);
            pid_C07_shooter_flywheel.setFF(Constants.ShooterControl.shooter_flywheel_pid.F);

        m_velocity_range = Constants.ShooterControl.shooter_velocity_range;
        m_velocity_offset = 0;
        
        m_enabled = false;
        m_safety_enabled = true;

        m_watchdog = new Watchdog(Constants.ShooterControl.shooter_watchdog_expire_time);
        m_logger = new RawLogger("flywheel/"+MatchData.getEventString()+".log");
    
        var parent = Shuffleboard.getTab("Robot Information").getLayout("Shooter Information");
            parent.addNumber("Flywheel Velocity", () -> { return enc_C07_shooter_flywheel.getVelocity(); });
            var child = parent.getLayout("Shooter Target Information");
                child.addNumber("Velocity Target", () -> { return m_target; });
                child.addNumber("Velocity Offset", () -> { return m_velocity_offset; });
                child.addBoolean("Vel. Reached", () -> { return isTargetReached(); } );
    }
    
    /**
     * Enables the turret flywheel
     */
    public void enable() {
        if (m_enabled)
            return;

        m_enabled = true;
        m_watchdog.feed();

        logger_writeEvent(ShooterEvent.kSubsystemEnabled, m_logger.getTimeSince());
    }

    /**
     * Disables the turret flywheel.
     */
    public void disable() {
        if (!m_enabled)
            return;
        
        mot_C07_shooter_flywheel.disable();
        mot_C18_shooter_feeder.disable();

        m_enabled = false;

        logger_writeEvent(ShooterEvent.kSubsystemDisabled, m_logger.getTimeSince());

        if (m_target != 0)
            

            logger_writeEvent(ShooterEvent.kSubsystemEnabled, m_logger.getTimeSince());
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

        m_target = m_velocity_range.clamp(target + m_velocity_offset);
        pid_C07_shooter_flywheel.setReference(m_target, ControlType.kVelocity);
       
        m_watchdog.feed();
    }

    /**
     * Gets the velocity of the turret flywheel.
     * 
     * @return The flywheel velocity.
     */
    public double getVelocity() {
        return enc_C07_shooter_flywheel.getVelocity();
    }

    /**
     * Checks if the turret flywheel velocity
     * has reached it's target setpoint.
     * 
     * @return If the flywheel velocity is at it's setpoint.
     */
    public boolean isTargetReached() {
        m_watchdog.feed();
        return (Math.abs(enc_C07_shooter_flywheel.getVelocity() - m_target) < Constants.ShooterControl.shooter_flywheel_target_thresh);
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

        mot_C07_shooter_flywheel.set(Range.scalar(target));
        
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

        mot_C18_shooter_feeder.set(Range.scalar(speed));
        
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

    /**
     * Shifts the flywheel velocity offset.
     * 
     * <p> The offset is applied to every
     * flywheel velocity target setpoint </p>
     * +
     * @param offset The velocity offset
     */
    public void shiftOffset(double shift) {
        m_velocity_offset += shift;

        SmartDashboard.putNumber("Flywheel Velocity Offset", m_velocity_offset);
    }

    /**
     * Get's the flywheel motor current output.
     * 
     * @return The current output.
     */
    public double getOutputCurrent() {
        return mot_C07_shooter_flywheel.getOutputCurrent();
    }
     
    @Override
    public void periodic() {
        if (m_enabled) {
            if (m_safety_enabled) {
                m_watchdog.update();
                if (m_watchdog.isExpired()) {
                    disable();
                }
            }
            logger_writeData();
        }

        SmartDashboard.putNumber("Actual Velocity", enc_C07_shooter_flywheel.getVelocity());
    }

    private void logger_writeEvent(ShooterEvent event, double arg) {
        //m_logger.write(1);
        //m_logger.write(arg);
    }

    private void logger_writeData() {
        /*m_logger.write(0);
        m_logger.write(enc_C07_shooter_flywheel.getVelocity());
        m_logger.write(mot_C07_shooter_flywheel.getOutputCurrent());
        m_logger.write(mot_C18_shooter_feeder.getOutputCurrent());*/
    }
}