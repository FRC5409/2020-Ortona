// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class Pneumatics {
        public static final int MODULE = 0;
        public static final double MIN_PSI = 0;
        public static final double MAX_PSI = 60;

    }


    public final class Climber {
        public static final int mot_port = 0;

        public static final int DIRECTION_EXTEND = 0;
        public static final int DIRECTION_RETRACT = 1;
        //TODO: determine the extension length
        public static final double EXTENSION_LENGTH = 0;
        public static final double RETRACTION_LENGTH = 0;

        public static final double ARM_SPEED = 1.8;

    }

    public static final class kIntakeIndexer{
        public static final int kIntakeMotor = 4;

        public static final int kRightIntakeSolenoid1 = 1;
        public static final int kRightIntakeSolenoid2 = 0;

        public static final int kLeftIntakeSolenoid1 = 3;
        public static final int kLeftIntakeSolenoid2 = 2;

        public static final int velocityMaxIntakeJam = 1000;
    }


    public final class kDriveTrain{

        // CAN IDs  (not initialized)
        public static final int CANLeftDriveFront = 4;
        public static final int CANRightDriveFront = 6;
        public static final int CANLeftDriveBack = 14;
        public static final int CANRightDriveBack = 15;
        
        // Current Limits
        public static final double CurrentLimit = 65;
        public static final double TriggerThresholdCurrent = 65;
        public static final double triggerThresholdTime = 0;

        // Double Solenoid
        public static final int ForwardChannel = 0;
        public static final int ReverseChannel = 1;

        // Drive Modes
        public static final int InitialDriveMode = 0;

        public static final int AADIL_DRIVE = 0;
        public static final int TANK_DRIVE = 1;

        // PID Controls
        public static final double P_Distance = 0;
        public static final double I_Distance = 0;
        public static final double D_Distance = 0;

        public static final double P_Angle = 0;
        public static final double I_Angle = 0;
        public static final double D_Angle = 0;

        // Speed limits for auto
        public static final double maxStraightSpeed = 1;
        public static final double maxTurnSpeed = 1;

        // Anti-tip compensation
        public static final double pitchCompensation = 0;
        public static final double rollCompensation = 0;

        public static final boolean startWithAntiTip = true;

        public static final double wheelSeparation = 1;

    }

    public final class kGyroSystem{

        public static final int CANPigeon = 0;


    }
}
