package org.frc.team5409.robot;

//import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;


import org.frc.team5409.robot.util.*;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.util.Units;

public final class Constants {
    public static final class Vision {
    // Target Constants
        public static final double vision_outerport_height = 90.75d/12.0d;
        

    // Robot configuration Constants
        public static final double vision_limelight_height = 42.5d/12.0d;

        public static final double vision_limelight_pitch = 10.2;


    // Timing Constants
        public static final double vision_acquisition_delay = 0.5;

        public static final double vision_aligned_thresh = 1.5;
    }

    public static final class ShooterControl {
    // General Configurations
        public static final double shooter_watchdog_expire_time = 0.8;

        public static final int shooter_turret_current_limit = 25;

        public static final int shooter_flywheel_current_limit = 50;

        public static final int shooter_feeder_current_limit = 25;


    // PID Configurations
        public static final PIDFConfig shooter_turret_pid = new PIDFConfig(
            0.03d, 0, 0, 0
        );

        public static final PIDFConfig shooter_flywheel_pid = new PIDFConfig(
            6e-5, 0, 0, 0.000231
        );


    // Range Configurations
        public static final Range shooter_turret_range = new Range(
            -270, 270
        );

        public static final Range shooter_velocity_range = new Range(
            0, 4500
        );

        public static final Range shooter_distance_range = new Range(
            10, 33
        );

        
    // Curve fitting Constants
        public static final SimpleEquation shooter_distance_rpm_curve = d -> {
            //d += (10.d/12.0d);
            //return 1.8*(d*d) - 26.8*(d) + 2929; //2nd POLY CURVE
            return 1326*Math.log(d) - 784; // LOG CURVE
            //return 978*Math.pow(d, 0.393); // POWER SERIES CURVE
            //return 1506*Math.log(d) - 1492;
            //return 2191.7 + 51.59 * d;
        };

        public static final String shooter_distance_rpm_curve_string = "1506 * ln(d + 0.83) - 1492";


    // Shooter Turret Constants
        public static final double shooter_turret_target_thresh = 0.4;

        public static final double shooter_turret_limit_left_angle = -270;

        public static final double shooter_turret_limit_center_angle = 0.5;

        public static final double shooter_turret_limit_right_angle = 270;

        public static final double shooter_calibrate_speed = 0.07;


    // Shooter Flywheel Constants
        public static final double shooter_flywheel_target_thresh = 0.98;


    // Smooth Sweep Constants (experimental)
        public static final double shooter_smooth_sweep_period = 3;

        public static final SimpleEquation shooter_smooth_sweep_func = t -> {
            return (Math.cos(2d*Math.PI*t/shooter_smooth_sweep_period)+1d)/2d*shooter_turret_range.magnitude+shooter_turret_range.min;
        };

        public static final SimpleEquation shooter_smooth_sweep_inverse = a -> {
            return shooter_smooth_sweep_period*Math.acos(2d*(a-shooter_turret_range.min)/shooter_turret_range.magnitude-1d)/(Math.PI*2d);
        };

        public static final double shooter_smooth_sweep_max_sweeps = 2;

    }

    public static final class Indexer {

        public static final int sampleTime = 24;

        public static final int TOF_Enter = 1; 

        public static final int TOF_Ball1 = 2; 

        public static final int TOF_Exit = 3; 

        public static final int m_indexer_id = 16; 

        public static final int currentLimit = 20;

    }

    public static final class Intake {

        public static final int kIntakeMotor = 10;

        public static final int kRightIntakeSolenoid1 = 0;

        public static final int kRightIntakeSolenoid2 = 0;

        public static final int kLeftIntakeSolenoid1 = 0;

        public static final int kLeftIntakeSolenoid2 = 0;

    }

    public static class DriveTrain {

        public static final int kLeftDriveFront = 14;

        public static final int kLeftDriveRear = 4;
        
        public static final int kRightDriveFront = 15;
        
        public static final int kRightDriveRear = 6;
        
        public static final int kShiftSolenoid1 = 6;
        
        public static final int kShiftSolenoid2 = 7;

        public static final double ksVolts = 0;
        
        public static final double kvVoltSecondsPerMeter = 0;
        
        public static final double kaVoltSecondsSquaredPerMeter = 0;

        public static final double kTrackwidthMeters = Units.inchesToMeters(19.5);

        // public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(
        //     kTrackwidthMeters);

        public static final int kEncoderCPR = 0;

        public static final double kWheelCircumferenceMeters = Units.inchesToMeters(Math.PI * 6);

        // public static final double kEncoderDistancePerPulse =
        //         // Assumes the encoders are directly mounted on the wheel shafts
        //         (kWheelDiameterMeters * Math.PI) / (double) kEncoderCPR;

        // encoder port id
        public static final int[] kLeftEncoderPorts = new int[] { 0, 1 };

        public static final int[] kRightEncoderPorts = new int[] { 0, 1 };

        public static final boolean kLeftEncoderReversed = false;

        public static final boolean kRightEncoderReversed = true;

        public static final boolean kGyroReversed = true;

        public static final double gearRatio = 18;

        public static final double neo_encoder_position = 42.0;

        //public static final double distanceCalculate = (Math.PI*kWheelDiameterMeters) / gearRatio;
    }
    public static class Hanging {

        // Motor Variables
        public static final int kID_motorSlave = 3;

        public static final int kID_motorMaster = 11;

        public static final double kExtendPosition = 10;

        public static final double kRetractPosition = 0;

        // Piston
        public static final int kForwardChannel = 4;

        public static final int kBackwardChannel = 5;
    }
}
