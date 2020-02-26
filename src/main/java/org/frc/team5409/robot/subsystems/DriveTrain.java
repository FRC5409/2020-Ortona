/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc.team5409.robot.Constants;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

public class DriveTrain extends SubsystemBase {

  private final AHRS m_navX;
  private final CANSparkMax mot_leftDriveFront_sparkmax_C14;
  private final CANSparkMax mot_rightDriveFront_sparkmax_C15;
  private final CANSparkMax mot_leftDriveRear_sparkmax_C4;
  private final CANSparkMax mot_rightDriveRear_sparkmax_C6;
  private SpeedControllerGroup m_leftMotors;
  private SpeedControllerGroup m_rightMotors;
  private final DifferentialDrive m_drive;
  private final CANEncoder m_leftEncoder;
  private final CANEncoder m_rightEncoder;
  private boolean m_antiTipToggle;
  private boolean m_fastShift;
  private final DifferentialDriveOdometry m_odometry;
  private static DoubleSolenoid dsl_gearSolenoid;

  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    mot_leftDriveFront_sparkmax_C14 = new CANSparkMax(Constants.DriveTrain.kLeftDriveFront, MotorType.kBrushless);
    mot_leftDriveFront_sparkmax_C14.restoreFactoryDefaults();
    mot_leftDriveFront_sparkmax_C14.setIdleMode(Constants.DriveTrain.idle);
    mot_leftDriveFront_sparkmax_C14.setSmartCurrentLimit(40);
    mot_leftDriveFront_sparkmax_C14.setInverted(true);
    mot_leftDriveFront_sparkmax_C14.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_leftDriveFront_sparkmax_C14.burnFlash();

    mot_leftDriveRear_sparkmax_C4 = new CANSparkMax(Constants.DriveTrain.kLeftDriveRear, MotorType.kBrushless);
    mot_leftDriveRear_sparkmax_C4.restoreFactoryDefaults();
    mot_leftDriveRear_sparkmax_C4.setIdleMode(Constants.DriveTrain.idle);
    mot_leftDriveRear_sparkmax_C4.setSmartCurrentLimit(40);
    mot_leftDriveRear_sparkmax_C4.setInverted(true);
    mot_leftDriveRear_sparkmax_C4.follow(mot_leftDriveFront_sparkmax_C14);
    mot_leftDriveRear_sparkmax_C4.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_leftDriveRear_sparkmax_C4.burnFlash();

    mot_rightDriveFront_sparkmax_C15 = new CANSparkMax(Constants.DriveTrain.kRightDriveFront, MotorType.kBrushless);
    mot_rightDriveFront_sparkmax_C15.restoreFactoryDefaults();
    mot_rightDriveFront_sparkmax_C15.setIdleMode(Constants.DriveTrain.idle);
    mot_rightDriveFront_sparkmax_C15.setSmartCurrentLimit(40);
    mot_rightDriveFront_sparkmax_C15.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_rightDriveFront_sparkmax_C15.burnFlash();

    mot_rightDriveRear_sparkmax_C6 = new CANSparkMax(Constants.DriveTrain.kRightDriveRear, MotorType.kBrushless);
    mot_rightDriveRear_sparkmax_C6.restoreFactoryDefaults();
    mot_rightDriveRear_sparkmax_C6.setIdleMode(Constants.DriveTrain.idle);
    mot_rightDriveRear_sparkmax_C6.setSmartCurrentLimit(40);
    mot_rightDriveRear_sparkmax_C6.follow(mot_rightDriveFront_sparkmax_C15);
    mot_rightDriveRear_sparkmax_C6.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_rightDriveRear_sparkmax_C6.burnFlash();

    // Sets speed control group to the corisponding motor
    

    m_drive = new DifferentialDrive(mot_leftDriveFront_sparkmax_C14, mot_rightDriveFront_sparkmax_C15);

    m_leftEncoder = mot_leftDriveFront_sparkmax_C14.getEncoder();
    m_rightEncoder = mot_rightDriveFront_sparkmax_C15.getEncoder();

    // Sets the distance per pulse for the encoders
    // https://www.chiefdelphi.com/t/encoder-distance-per-pulse/156742

    m_leftEncoder.setPositionConversionFactor(
        (Constants.DriveTrain.neo_encoder_position) * Constants.DriveTrain.kEncoderDistancePerPulse);
    m_rightEncoder.setPositionConversionFactor(
        (Constants.DriveTrain.neo_encoder_position) * Constants.DriveTrain.kEncoderDistancePerPulse);

    resetEncoders();
    m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
    
    dsl_gearSolenoid = new DoubleSolenoid(6, 7);
    
    // Set intial shift value to slow
    m_fastShift = false;

    // Calibrate the gyro
    m_navX = new AHRS(SPI.Port.kMXP);
    // Add NAVX calibration here

    // Set intial toggle value to true
    m_antiTipToggle = true;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Update the odometry in the periodic block
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), m_leftEncoder.getPosition(), m_rightEncoder.getPosition());

    // Get the encoder positions and display in smart dashboard
    SmartDashboard.putNumber("kleftencoder value", m_leftEncoder.getPosition());
    SmartDashboard.putNumber("krightencoder value", m_rightEncoder.getPosition());
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
    dsl_gearSolenoid.set(Value.kForward);
    m_fastShift = true;
  }

  /**
   * Method to shift to slow gear
   */
  public void slowShift() {
    dsl_gearSolenoid.set(Value.kReverse);
    m_fastShift = false;
  }

  /**
   * Method to return shift value
   * 
   * @return Current fast shift value
   */
  public boolean getShiftValue() {
    return m_fastShift;
  }

  public void arcadeDrive(double fwd, double rot) {
    m_drive.arcadeDrive(fwd, rot);
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(m_leftEncoder.getVelocity(), m_rightEncoder.getVelocity());
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void manualDrive(final double acceleration, final double deceleration, final double turn) {
    final double accelerate = acceleration - deceleration;
    m_drive.arcadeDrive(turn, accelerate);
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts  the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    m_leftMotors.setVoltage(leftVolts);
    m_rightMotors.setVoltage(-rightVolts);
    m_drive.feed();
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    m_leftEncoder.setPosition(0);
    m_rightEncoder.setPosition(0);
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (m_leftEncoder.getPosition() + m_rightEncoder.getPosition()) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   * 
   * @return the left drive encoder
   */
  public CANEncoder getLeftEncoder() {
    return m_leftEncoder;
  }

  public double getLeftEncoderPosition() {
    return m_leftEncoder.getPosition();
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public CANEncoder getRightEncoder() {
    return m_rightEncoder;
  }

  public double getRightEncoderPosition() {
    return m_rightEncoder.getPosition();
  }

  /**
   * Sets the max output of the drive. Useful for scaling the drive to drive more
   * slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void zeroHeading() {
    m_navX.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Math.IEEEremainder(m_navX.getAngle(), 360) * (Constants.DriveTrain.kGyroReversed ? -1.0 : 1.0);
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return m_navX.getRate() * (Constants.DriveTrain.kGyroReversed ? -1.0 : 1.0);
  }

  public void setIdleBrake() {
    setMotorIdleMode(IdleMode.kBrake);
  }

  private void setMotorIdleMode(IdleMode idlemode) {
    mot_rightDriveRear_sparkmax_C6.setIdleMode(idlemode);
    mot_rightDriveFront_sparkmax_C15.setIdleMode(idlemode);
    mot_leftDriveFront_sparkmax_C14.setIdleMode(idlemode);
    mot_leftDriveRear_sparkmax_C4.setIdleMode(idlemode);
  }

  public void setIdleCoast() {
    setMotorIdleMode(IdleMode.kCoast);
  }

  public void setLeftMotors(double speed) {
    m_leftMotors.set(speed);
  }

  public void setRightMotors(double speed) {
    m_rightMotors.set(speed);
  }

  public void auto() {

    if (m_leftEncoder.getPosition() * Constants.DriveTrain.distanceCalculate <= (1)) {
      m_leftMotors.set(0.5);
      m_rightMotors.set(0.5);

    } else {

      m_leftMotors.set(0);
      m_rightMotors.set(0);

    }

  }
}
