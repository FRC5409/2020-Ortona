package org.frc.team5409.robot.util;

/**
 * Outlines functionality for subsystems
 * that can be enabled and disabled.
 * 
 * @author Keith Davies
 */
public abstract interface Toggleable {
    /**
     * Enables the toggleable subsystem.
     */
    public void enable();

    /**
     * Disables the toggleable subsystem.
     */
    public void disable();

    /**
     * Checks whether the toggleable subsystem
     * is enabled or disabled.
     * 
     * @return The toggleable subsystems enabled state.
     */
    public boolean isEnabled();
}