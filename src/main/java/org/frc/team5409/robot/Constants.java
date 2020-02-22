package org.frc.team5409.robot;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;


import org.frc.team5409.robot.util.*;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public final class Constants {
    public static final class Vision {
//========================================================
    // Target Constants
        public static final double vision_outerport_height = 89.75d/12.0d;

//========================================================
    // Robot configuration Constants
        public static final double vision_limelight_height = 103.1d/12.0d;

        public static final double vision_limelight_pitch = 10;
    }

    public static final class TurretControl {
//========================================================
    // General Configurations
        public static final double turret_watchdog_expire_time = 0.8;

        public static final int turret_rotation_current_limit = 25;

        public static final int turret_flywheel_current_limit = 40;

        public static final int turret_feeder_current_limit = 25;
//========================================================
    // PID Configurations
        public static final PIDFConfig pid_turret_rotation = new PIDFConfig(0, 0, 0, 0);

        public static final PIDFConfig pid_turret_flywheel = new PIDFConfig(6e-5, 0, 0, 0.000015);

//========================================================
    // Range Configurations
        public static final Range turret_rotation_range = new Range(-270, 270);

        public static final Range turret_velocity_range = new Range(0, 7000);

        public static final Range turret_distance_range = new Range(15, 33);

//========================================================
    // Curve fitting Constants
        public static final SimpleEquation turret_distance_rpm_curve = d -> {
            d += (10.d/12.0d);
            //return 1.8*(d*d) - 26.8*(d) + 2929; //2nd POLY CURVE
            return 1326*Math.log(d) - 784; // LOG CURVE
            //return 978*Math.pow(d, 0.393); // POWER SERIES CURVE
        };

//========================================================
    // Turret Rotation Constants
        public static final double turret_rotation_target_thresh = 0;

        public static final double turret_rotation_limit_left_angle = -270;

        public static final double turret_rotation_limit_center_angle = 0.5;

        public static final double turret_rotation_limit_right_angle = 270;

        public static final double turret_sweep_speed = 0.5;

//========================================================
    // Turret Flywheel Constants
        public static final double turret_flywheel_target_thresh = 0.93;

        public static final double turret_flywheel_rpm_scale = 4.25;
    }

    public static final class Indexer {

        public static final int sampleTime = 24;

        public static final int TOF_Enter = 2; 

        public static final int TOF_Ball1 = 3; 

        public static final int TOF_Exit = 1; 

        public static final int m_Indexer_neo550_C16 = 16; 

        public static final int currentLimit = 20; 

        public static final int rangeEnter_1 = 125; 
        
        public static final int rangeEnter_2 = 50; 

        public static final int rangeBall1_1 = 130; 

        public static final int rangeBall1_2 = 100; 

        public static final int rangeExit_1 = 150; 

        public static final int rangeExit_2 = 100; 

    }

    public static final class Intake {

        public static final int kIntakeMotor = 0;

        public static final int kRightIntakeSolenoid1 = 0;

        public static final int kRightIntakeSolenoid2 = 0;

        public static final int kLeftIntakeSolenoid1 = 0;

        public static final int kLeftIntakeSolenoid2 = 0;

		public void retract() {
		}
    }

    public static class Trajectory{
        
        public static final double ksVolts = 0;

        public static final double kvVoltSecondsPerMeter = 0;
        
        public static final double kaVoltSecondsSquaredPerMeter = 0;

        public static final double kMaxSpeedMetersPerSecond = 0;
        
        public static final double kMaxAccelerationMetersPerSecondSquare = 0;
        
        public static final double kRamseteB = 0;
        
        public static final double kRamseteZeta = 0;
        
        public static final double kPDriveVel = 0;

        public static final double kTrackwidthMeters = 0;
    
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(
            kTrackwidthMeters);
    }

    public static class DriveTrain{

        public static final IdleMode idle = IdleMode.kBrake;

        public static final double loopRampRate= 0.5;
        public static final int kLeftDriveFront = 13;

        public static final int kLeftDriveRear = 4;
        
        public static final int kRightDriveFront = 15;
        
        public static final int kRightDriveRear = 6;
        
        public static final int kIntakeMotor = 0;
        
        public static final int kRightIntakeSolenoid1 = 0;
        
        public static final int kRightIntakeSolenoid2 = 0;
        
        public static final int kLeftIntakeSolenoid1 = 0;
        
        public static final int kLeftIntakeSolenoid2 = 0;

        public static final double ksVolts = 0;
        
        public static final double kvVoltSecondsPerMeter = 0;
        
        public static final double kaVoltSecondsSquaredPerMeter = 0;

        public static final double kTrackwidthMeters = 0;
        public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(
            kTrackwidthMeters);

        public static final int kEncoderCPR = 0;

        public static final double kWheelDiameterMeters = 0;

        public static final double kEncoderDistancePerPulse =
                // Assumes the encoders are directly mounted on the wheel shafts
                (kWheelDiameterMeters * Math.PI) / (double) kEncoderCPR;

        // encoder port id
        public static final int[] kLeftEncoderPorts = new int[] { 0, 1 };

        public static final int[] kRightEncoderPorts = new int[] { 0, 1 };

        public static final boolean kLeftEncoderReversed = false;

        public static final boolean kRightEncoderReversed = true;

        public static final boolean kGyroReversed = true;

        public static final double gearRatio = 0;

        public static final double neo_encoder_position = 42.0;

        public static final double distanceCalculate = (Math.PI*kWheelDiameterMeters) / gearRatio;
    }
    public static class Hanging{

        //PID Variables
        public static final double kP = 0.1;
        public static final double kI = 1e-4;
        public static final double kD = 1;
        public static final double kIz = 0;
        public static final double kFF = 0;
        public static final double kMaxOutput = 0.1;
        public static final double kMinOutput = -0.1;

        //Neo Variables
        public static final int NEO1_ID = 3;
        public static final int NEO2_ID = 5;
        public static final double EXTEND_NEO_POS = 10;
        public static final double RETRACT_NEO_POS = 0;

        //Piston
        public static final int FORWARD_CHANEL = 1;
        public static final int BACKWARD_CHANEL = 2;
    }
}
