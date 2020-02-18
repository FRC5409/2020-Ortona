package org.frc.team5409.churro.subsystems;

import java.util.concurrent.atomic.AtomicReference;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.Vec2;
import frc.robot.util.Vec3;
import edu.wpi.first.networktables.*;


/**
 * Limelight subsystem.
 * 
 * Facilitates the control and access
 * of limelight hardware through a software
 * interface.
 */
public class Limelight extends SubsystemBase {
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
     * Construct subsystem and initialize limeight communication
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
        m_target_data            = TargetType.NONE;

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
        
        setLedMode(LedMode.LED_OFF);
    }


    /**
     * Set Camera Mode on limelight
     * 
     * @param camera_mode Camera Mode
     * 
     * @see CameraMode
     */
    public void setCameraMode(CameraMode camera_mode) {
        m_data_entry_cam_mode.setDouble(camera_mode.get());
    }

    /**
     * Set Led Mode on limelight
     * 
     * @param led_mode Led Mode
     * 
     * @see LedMode
     */
    public void setLedMode(LedMode led_mode) {
        m_data_entry_led_mode.setDouble(led_mode.get());
    }

    /**
     * Set Pipeline Index on limelight
     * 
     * @param pipeline_index Pipeline Index
     * 
     * @see PipelineIndex
     */
    public void setPipelineIndex(PipelineIndex pipeline_index) {
        m_data_entry_pipeline.setDouble(pipeline_index.get());
    }

    /**
     * Get current Tracking Target from Limelight Pipeline.
     * 
     * @return Limelight Target
     */
    public Vec2 getTarget() {
        return new Vec2(m_track_data.x, m_track_data.y);
    }

    /**
     * Get current Tracking Target Area from Limelight Pipeline.
     * 
     * @return Limelight Target Area
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
        return m_target_data != TargetType.NONE;
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
                m_target_data = TargetType.OUTER_PORT;
            } else {
                m_target_data = TargetType.NONE;
            }
        }
    }
}