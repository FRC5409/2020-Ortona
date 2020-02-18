package org.frc.team5409.robot;

import com.revrobotics.CANSparkMax.IdleMode;

import org.frc.team5409.robot.util.PIDFConfig;
import org.frc.team5409.robot.util.Range;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;

public final class Constants {

    public static final class TurretControl {
        public static final PIDFConfig pid_turret_rotation = new PIDFConfig(0, 0, 0, 0);

        public static final PIDFConfig pid_turret_flywheel = new PIDFConfig(6e-5, 0, 0, 0.000015);

        public static final Range turret_rotation_range = new Range(-270, 270);

        public static final Range turret_velocity_range = new Range(0, 7000);

        public static final double turret_rotation_target_thresh = 0;

        public static final double turret_flywheel_diameter = 0;

        public static final double turret_intake_enable_thresh = 1000;

        public static final double turret_watchdog_expire_time = 0.8;

        public static final double turret_limit_left_angle = -270;

        public static final double turret_limit_center_angle = 270;

        public static final double turret_limit_right_angle = 0.5;

        public static final int turret_rotation_current_limit = 25;

        public static final int turret_flywheel_current_limit = 40;

        public static final int turret_feeder_current_limit = 25;

        public static final double turret_sweep_speed = 0.5;

        public static final double turret_flywheel_rpm_scale = 4.45;
    }

    public static final class Indexer {

        public static final int sampleTime = 24;

        public static final int TOF_Enter = 2; 

        public static final int TOF_Ball1 = 3; 

        public static final int TOF_Exit = 1; 

        public static final int m_Indexer_neo550_C16 = 16; 

        public static final int currentLimit = 20; 

    }

    public static final class Intake {

        public static final int kIntakeMotor = 0;

        public static final int kRightIntakeSolenoid1 = 0;

        public static final int kRightIntakeSolenoid2 = 0;

        public static final int kLeftIntakeSolenoid1 = 0;

        public static final int kLeftIntakeSolenoid2 = 0;
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
public static final double kWheelDiameterMeters = 0.457;
public static final double kEncoderDistancePerPulse =
        // Assumes the encoders are directly mounted on the wheel shafts
        (kWheelDiameterMeters * Math.PI) / (double) kEncoderCPR;

// encoder port id
public static final int[] kLeftEncoderPorts = new int[] { 0, 1 };
public static final int[] kRightEncoderPorts = new int[] { 0, 1 };

public static final boolean kLeftEncoderReversed = false;
public static final boolean kRightEncoderReversed = true;
public static final boolean kGyroReversed = true;
    }
}
