package org.frc.team5409.robot.util;

public abstract interface AutoCommand {
    public enum AutonomousState {
        kShooting, kDriving, kIntaking, kFinished
    }

    public boolean getState(AutonomousState state);
}