package org.frc.team5409.robot;

//import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;


import org.frc.team5409.robot.util.*;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public final class Constants {
    public static final class Vision {
    // Target Constants
        public static final double vision_outerport_height = 90.75d/12.0d;


    // Robot configuration Constants
        public static final double vision_limelight_height = 41.1d/12.0d;

        public static final double vision_limelight_pitch = 12.4;


    // Timing Constants
        public static final double vision_acquisition_delay = 0.5;
    }

    public static final class ShooterControl {
    // General Configurations
        public static final double shooter_watchdog_expire_time = 0.8;

        public static final int shooter_turret_current_limit = 25;

        public static final int shooter_flywheel_current_limit = 40;

        public static final int shooter_feeder_current_limit = 25;


    // PID Configurations
        public static final PIDFConfig shooter_turret_pid = new PIDFConfig(0.03, 0, 0, 0);

        public static final PIDFConfig shooter_flywheel_pid = new PIDFConfig(6e-5, 0, 0, 0.000015);


    // Range Configurations
        public static final Range shooter_turret_range = new Range(-270, 270);

        public static final Range shooter_velocity_range = new Range(0, 4500);

        public static final Range shooter_distance_range = new Range(15, 33);

        
    // Curve fitting Constants
        public static final SimpleEquation shooter_distance_rpm_curve = d -> {
            d += (10.d/12.0d);
            //return 1.8*(d*d) - 26.8*(d) + 2929; //2nd POLY CURVE
            //return 1326*Math.log(d) - 784; // LOG CURVE
            //return 978*Math.pow(d, 0.393); // POWER SERIES CURVE
            return 1506*Math.log(d) - 1492;
        };

        public static final String shooter_distance_rpm_curve_string = "1506 * ln(d + 0.83) - 1492";


    // Shooter Turret Constants
        public static final double shooter_turret_target_thresh = 0;

        public static final double shooter_turret_limit_left_angle = -270;

        public static final double shooter_turret_limit_center_angle = 0.5;

        public static final double shooter_turret_limit_right_angle = 270;

        public static final double shooter_calibrate_speed = 0.07;


    // Smooth Sweep Constants (experimental)
        public static final double shooter_smooth_sweep_period = 10;

        public static final SimpleEquation shooter_smooth_sweep_func = t -> {
            return Math.cos(2d*Math.PI*t/shooter_smooth_sweep_period+1)/2d*shooter_turret_range.magnitude+shooter_turret_range.min;
        };

        public static final SimpleEquation shooter_smooth_sweep_inverse = a -> {
            return shooter_smooth_sweep_period*Math.acos(2d*(a-shooter_turret_range.min)/shooter_turret_range.magnitude-2d)/(Math.PI*2d);
        };

        public static final double shooter_smooth_sweep_max_sweeps = 2;


    // Shooter Flywheel Constants
        public static final double shooter_flywheel_target_thresh = 0.93;

        public static final double shooter_flywheel_rpm_scale = 4.25;
    }

    public static final class Indexer {

        public static final int sampleTime = 24;

        public static final int TOF_Enter = 1; 

        public static final int TOF_Ball1 = 2; 

        public static final int TOF_Exit = 3; 

        public static final int m_Indexer_neo550_C16 = 16; 

        public static final int currentLimit = 20; 

        public static final int rangeEnter_1 = 
        50; 
        
        public static final int rangeEnter_2 = 150; 

        public static final int rangeBall1_1 = 140; 

        public static final int rangeBall1_2 = 130; 

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

        public static final int kLeftDriveFront = 14;

        public static final int kLeftDriveRear = 4;
        
        public static final int kRightDriveFront = 15;
        
        public static final int kRightDriveRear = 6;
        
        public static final int kShiftSolenoid1 = 6;
        
        public static final int kShiftSolenoid2 = 7;

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
        public static final int NEO_SLAVE_ID = 3;

        public static final int NEO_MASTER_ID = 5;

        public static final double EXTEND_NEO_POS = 10;

        public static final double RETRACT_NEO_POS = 0;

        //Piston
        public static final int FORWARD_CHANEL = 1;

        public static final int BACKWARD_CHANEL = 2;

        //Time Of Flight
        public static final int TOF_ID = 4;

        public static final double TOF_RANGE = 10;
        
        public static final double TOF_SAMPLE_TIME = 24;

        // Gear Shift

        public static final double shiftThreshF = 0; // Encoder pulse rate threshold for fast shift
        public static final double shiftThreshS = 0; // Encoder pulse raet threshold for slow shift
    }
}
