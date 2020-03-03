/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc.team5409.robot.Constants;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

public class DriveTrain extends SubsystemBase {

  private final AHRS m_navX;
  private final CANSparkMax mot_leftDriveMaster;
  private final CANSparkMax mot_rightDriveMaster;
  private final CANSparkMax mot_leftDriveSlave;
  private final CANSparkMax mot_rightDriveSlave;
  private final DifferentialDrive m_drive;
  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;
  private boolean m_antiTipToggle;
  private static DoubleSolenoid dsl_gearSolenoid;

  // Aadil's Code
  DifferentialDriveKinematics m_kinematics;
  DifferentialDriveOdometry m_odometry;
  SimpleMotorFeedforward m_feedforward;
  PIDController m_leftPidController;
  PIDController m_rightPidController;

  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    // Left Drive Master
    mot_leftDriveMaster = new CANSparkMax(Constants.DriveTrain.kLeftDriveFront, MotorType.kBrushless);
    mot_leftDriveMaster.restoreFactoryDefaults();
    mot_leftDriveMaster.setIdleMode(IdleMode.kCoast);
    mot_leftDriveMaster.setSmartCurrentLimit(60);
    mot_leftDriveMaster.burnFlash();

    // Left Drive Slave
    mot_leftDriveSlave = new CANSparkMax(Constants.DriveTrain.kLeftDriveRear, MotorType.kBrushless);
    mot_leftDriveSlave.restoreFactoryDefaults();
    mot_leftDriveSlave.setIdleMode(IdleMode.kCoast);
    mot_leftDriveSlave.setSmartCurrentLimit(60);
    mot_leftDriveSlave.burnFlash();

    // Left Slave follow Master
    mot_leftDriveSlave.follow(mot_leftDriveMaster);
    mot_leftDriveMaster.setInverted(true);

    // Right Drive Master
    mot_rightDriveMaster = new CANSparkMax(Constants.DriveTrain.kRightDriveFront, MotorType.kBrushless);
    mot_rightDriveMaster.restoreFactoryDefaults();
    mot_rightDriveMaster.setIdleMode(IdleMode.kCoast);
    mot_rightDriveMaster.setSmartCurrentLimit(60);
    mot_rightDriveMaster.burnFlash();

    // Right Drive Slave
    mot_rightDriveSlave = new CANSparkMax(Constants.DriveTrain.kRightDriveRear, MotorType.kBrushless);
    mot_rightDriveSlave.restoreFactoryDefaults();
    mot_rightDriveSlave.setIdleMode(IdleMode.kCoast);
    mot_rightDriveSlave.setSmartCurrentLimit(60);
    mot_rightDriveSlave.burnFlash();

    // Right Slave follow Master
    mot_rightDriveSlave.follow(mot_rightDriveMaster);
    mot_rightDriveSlave.setInverted(false);    

    // Differential Drive
    m_drive = new DifferentialDrive(mot_leftDriveMaster, mot_rightDriveMaster);

    // CAN Encoder
    m_leftEncoder = mot_leftDriveMaster.getEncoder();
    m_rightEncoder = mot_rightDriveMaster.getEncoder();
    resetEncoders();
    
    dsl_gearSolenoid = new DoubleSolenoid(Constants.DriveTrain.kShiftSolenoid1, Constants.DriveTrain.kShiftSolenoid2);

    // Calibrate the gyro
    m_navX = new AHRS(SPI.Port.kMXP);
    resetHeading();

    // Add NAVX calibration here

    // Set intial toggle value to false
    m_antiTipToggle = false;

    // Aadil's Code
    m_kinematics = new DifferentialDriveKinematics(Constants.DriveTrain.kTrackwidthMeters);

    m_odometry = new DifferentialDriveOdometry(getHeading());

    m_feedforward = new SimpleMotorFeedforward(0, 0, 0);
    
    m_leftPidController = new PIDController(0, 0, 0);
    m_rightPidController = new PIDController(0, 0, 0);
  }

  /**
   * Gets left encoder distance in meters
   */
  public double getLeftEncoderDistance() {
    return m_leftEncoder.getPosition() * Constants.DriveTrain.kWheelCircumferenceMeters;
  }

  /**
   * Gets right encoder distance in meters
   */
  public double getRightEncoderDistance() {
    return m_rightEncoder.getPosition() * Constants.DriveTrain.kWheelCircumferenceMeters;
  }

  /**
   * Gets average encoder distance in meters
   */
  public double getAvgEncoderDistance() {
    return getLeftEncoderDistance() + getRightEncoderDistance() / 2;
  }
  public Rotation2d getHeading() {
    return Rotation2d.fromDegrees(-m_navX.getAngle());
  }

  public DifferentialDriveKinematics getKinematics() {
    return m_kinematics;
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(
      mot_leftDriveMaster.getEncoder().getVelocity() / Constants.DriveTrain.gearRatio * Constants.DriveTrain.kWheelCircumferenceMeters / 60,
      mot_rightDriveMaster.getEncoder().getVelocity() / Constants.DriveTrain.gearRatio * Constants.DriveTrain.kWheelCircumferenceMeters / 60
      );
  }

  public SimpleMotorFeedforward getFeedforward() {
    return m_feedforward;
  }

  public PIDController getLeftPIDController() {
    return m_leftPidController;
  }
  
  public PIDController getRightPIDController() {
    return m_rightPidController;
  }

  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  // public Pose2d getPose() {
  //   return pose;
  // }

  public void setOutputVoltage(double leftVolts, double rightVolts) {
    mot_leftDriveMaster.setVoltage(leftVolts);
    mot_rightDriveMaster.setVoltage(rightVolts);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Update the odometry in the periodic block
    m_odometry.update(getHeading(), getLeftEncoderDistance(), getRightEncoderDistance());
    // pose = odometry.update(getHeading(), getLeftEncoderDistance(), getRightEncoderDistance());

    // Get the encoder positions and display in smart dashboard
    SmartDashboard.putNumber("kleftencoder value", getLeftEncoderPosition());
    SmartDashboard.putNumber("krightencoder value", getRightEncoderPosition());
  }

  /**
   * Method to get AntiTip toggle value
   * 
   * @return The toggle value
   */
  public boolean getAntiTip() {
    return m_antiTipToggle;
  }

  /**
   * Method to turn AntiTip on
   */
  public void turnAntiTipOn() {
    m_antiTipToggle = true;
  }

  /**
   * Method to turn AntiTip off
   */
  public void turnAntiTipOff() {
    m_antiTipToggle = false;
  }

  /**
   * Method to get NavX roll angle
   * 
   * @return The roll angle
   */
  public double getRollAngle() {
    return m_navX.getRoll();
  }

  /**
   * Method to get NavX pitch angle
   * 
   * @return The pitch angle
   */
  public double getPitchAngle() {
    return m_navX.getPitch();
  }

  /**
   * Method to shift to fast gear
   */
  public void fastShift() {
    dsl_gearSolenoid.set(Value.kReverse);
  }

  /**
   * Method to shift to slow gear
   */
  public void slowShift() {
    dsl_gearSolenoid.set(Value.kForward);
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    m_leftEncoder.setPosition(0);
    m_rightEncoder.setPosition(0);
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry() {
    resetEncoders();
    resetHeading();
    m_odometry.resetPosition(new Pose2d(), getHeading());
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void resetHeading() {
    m_navX.reset();
  }

  /**
   * Allows for Basic Arcade Drive
   * 
   * @param acceleration
   * @param deceleration
   * @param turn
   */
  public void arcadeDrive(final double acceleration, final double deceleration, final double turn) {
    final double accelerate = acceleration - deceleration;
    m_drive.arcadeDrive(turn, accelerate);
  }

  /**
   * Allows for Basic Arcade Drive
   * 
   * @param fwd
   * @param rot
   */
  public void arcadeDrive(double fwd, double rot) {
    m_drive.arcadeDrive(fwd, rot);
  }

  public double getLeftEncoderPosition() {
    return m_leftEncoder.getPosition();
  }

  public double getRightEncoderPosition() {
    return m_rightEncoder.getPosition();
  }

  public void setLeftMotors(double speed) {
    mot_leftDriveMaster.set(speed);
  }

  public void setRightMotors(double speed) {
    mot_rightDriveMaster.set(speed);
  }
}
