package org.frc.team5409.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.*;

import org.frc.team5409.robot.util.*;


/**
 * Facilitates the control and access
 * of limelight hardware.
 * 
 * 
 * http://10.54.9.99:5801/ 
 * @author Keith Davies
 */ //TODO FIX LINK TO LIMELIGHT
public class Limelight extends SubsystemBase implements Toggleable {
    /**
     * The Led mode of the limelight.
     */
    public enum LedMode {
        kModePipeline(0), kModeOff(1), kModeBlink(2), kModeOn(3);

        LedMode(double value) {
            this.value = value;
        }

        public final double value;
    }

    /**
     * The camera mode of the limelight.
     */
    public enum CameraMode {
        kModeVision(0), kModeDriver(1);

        CameraMode(double value) {
            this.value = value;
        }

        public final double value;
    }

    /**
     * The type of target seen by the limelight.
     */
    public enum TargetType {
        kOuterPort, kLowerPort, kControlPanel, kNone
    }

    private NetworkTable      m_limelight_data;

    private NetworkTableEntry m_data_entry_tx,
                              m_data_entry_ty,
                              m_data_entry_ta;

    private NetworkTableEntry m_data_entry_led_mode, 
                              m_data_entry_cam_mode,
                              m_data_entry_pipeline;

    private NetworkTableEntry m_data_entry_has_targets;

    private Vec3              m_track_data;
    private TargetType        m_target_data;

    private boolean           m_enabled;

    /**
     * Constructs the Limelight subsystem.
     */
    public Limelight() {
        m_limelight_data         = NetworkTableInstance.getDefault().getTable("limelight");

        m_data_entry_tx          = m_limelight_data.getEntry("tx");
        m_data_entry_ty          = m_limelight_data.getEntry("ty");
        m_data_entry_ta          = m_limelight_data.getEntry("ta");

        m_data_entry_cam_mode    = m_limelight_data.getEntry("camMode");
        m_data_entry_led_mode    = m_limelight_data.getEntry("ledMode");
        m_data_entry_pipeline    = m_limelight_data.getEntry("pipeline");
        
        m_data_entry_has_targets = m_limelight_data.getEntry("tv");

        m_track_data             = new Vec3(0,0,0);
        m_target_data            = TargetType.kNone;

        m_enabled                = false;
    }

    /**
     * Enables the Limelight.
     */
    public void enable() {
        m_enabled = true;
    }

    /**
     * Disables the Limelight.
     */
    public void disable() {
        m_enabled = false;
        m_target_data = TargetType.kNone;
        setLedMode(LedMode.kModeOff);
    }

    /**
     * Checks whether or not the limelight subsystem
     * is currently enabled.
     * 
     * @return The subsystems enabled state.
     */
    public boolean isEnabled() {
        return m_enabled;
    }

    /**
     * Set's the camera mode of the limelight.
     * 
     * @param mode The camera mode.
     * 
     * @see CameraMode
     */
    public void setCameraMode(CameraMode mode) {
        m_data_entry_cam_mode.setDouble(mode.value);
    }

    /**
     * Set's the led mode of the limelight.
     * 
     * @param mode The led mode.
     * 
     * @see LedMode
     */
    public void setLedMode(LedMode mode) {
        m_data_entry_led_mode.setDouble(mode.value);
    }

    /**
     * Set's the pipeline index of the limelight.
     * 
     * @param index The pipeline index. [0-9]
     */
    public void setPipelineIndex(double index) {
        m_data_entry_pipeline.setDouble(Range.clamp(0, index, 9));
    }

    /**
     * Get's the current tracking target off the limelight
     * pipeline.
     * 
     * @return The limelight target.
     */
    public Vec2 getTarget() {
        return new Vec2(m_track_data.x, m_track_data.y);
    }

    /**
     * Get's the type of target seen by the limelight.
     * 
     * @return The active target.
     */
    public TargetType getTargetType() {
        return m_target_data;
    }

    /**
     * Get's the current tracking target area.
     * 
     * @return The active target area.
     */
    public double getTargetArea() {
        return m_track_data.z;
    }

    /**
     * Checks to see if the limelight is currently tracking any targets.
     * 
     * @return Whether or not the limelight is tracking a target.
     */
    public boolean hasTarget() {
        return m_target_data != TargetType.kNone;
    }

    private void internal_updateTarget() {
        m_track_data.x = m_data_entry_tx.getDouble(m_track_data.x);
        m_track_data.y = m_data_entry_ty.getDouble(m_track_data.y);
        m_track_data.z = m_data_entry_ta.getDouble(m_track_data.z);
    }

    private boolean internal_hasTarget() {
        return m_data_entry_has_targets.getDouble(0) == 1;
    }
        
    @Override
    public void periodic() {
        if (m_enabled) {
            if (internal_hasTarget()) {
                internal_updateTarget();
                m_target_data = TargetType.kOuterPort;
            } else {
                m_target_data = TargetType.kNone;
            }
        }
    }
}