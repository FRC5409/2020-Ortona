package org.frc.team5409.robot.util;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * Simple utility for retrieving match
 * information and data.
 * 
 * @author Keith Davies
 */
public final class MatchData {
    private MatchData() {
    }

    /**
     * Gets the name of the current event match.
     * 
     * <p> [Match type] [Match number] - [Alliance Color] </p>
     */
    public static final String getMatchString() {
        DriverStation inst = DriverStation.getInstance();
        String match_string = "";

        switch (inst.getMatchType()) {
            case None: match_string = "Unknown"; break;
            case Practice: match_string = "Practice"; break;
            case Qualification: match_string = "Qualification"; break;
            case Elimination: match_string = "Elimination"; break;
        }

        if (inst.getReplayNumber() != 0) {
            match_string += String.format(" [%d, R%d] - ", inst.getMatchNumber(), inst.getReplayNumber());
        } else  {
            match_string += " ["+Integer.toString(inst.getMatchNumber())+"] - ";
        }

        switch (inst.getAlliance()) {
            case Red: match_string += "Red Alliance"; break;
            case Blue: match_string += "Blue Alliance"; break;
            case Invalid: match_string += "Invalid Alliance"; break;
        }

        return match_string;
    }

    /**
     * Gets the name of the event location.
     */
    public static final String getEventString() {
        return DriverStation.getInstance().getEventName();
    }
}