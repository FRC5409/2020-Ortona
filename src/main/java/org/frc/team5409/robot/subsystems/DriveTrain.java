/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc.team5409.robot.Constants;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ExternalFollower;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

public class DriveTrain extends SubsystemBase {

  private final AHRS m_navX;
  private final CANSparkMax mot_leftDriveFront_sparkmax_C14;
  private final CANSparkMax mot_rightDriveFront_sparkmax_C15;
  private final CANSparkMax mot_leftDriveRear_sparkmax_C4;
  private final CANSparkMax mot_rightDriveRear_sparkmax_C6;
  private final DifferentialDrive m_drive;
  private final RelativeEncoder m_leftEncoder;
  private final RelativeEncoder m_rightEncoder;
  private boolean m_antiTipToggle;
  private DifferentialDriveOdometry m_odometry;
  private static DoubleSolenoid dsl_gearSolenoid;

  /**
   * Creates a new DriveTrain.
   */
  public DriveTrain() {
    mot_leftDriveFront_sparkmax_C14 = new CANSparkMax(Constants.DriveTrain.kLeftDriveFront, MotorType.kBrushless);
    
    mot_leftDriveFront_sparkmax_C14.restoreFactoryDefaults();
    mot_leftDriveFront_sparkmax_C14.setIdleMode(Constants.DriveTrain.idle);
    mot_leftDriveFront_sparkmax_C14.setSmartCurrentLimit(60);
    mot_leftDriveFront_sparkmax_C14.setInverted(true);
    mot_leftDriveFront_sparkmax_C14.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_leftDriveFront_sparkmax_C14.burnFlash();

    mot_leftDriveRear_sparkmax_C4 = new CANSparkMax(Constants.DriveTrain.kLeftDriveRear, MotorType.kBrushless);
    mot_leftDriveRear_sparkmax_C4.restoreFactoryDefaults();
    mot_leftDriveRear_sparkmax_C4.setIdleMode(Constants.DriveTrain.idle);
    mot_leftDriveRear_sparkmax_C4.setSmartCurrentLimit(60);
    mot_leftDriveRear_sparkmax_C4.setInverted(true);
    //follower fix
    mot_leftDriveFront_sparkmax_C14.follow(ExternalFollower.kFollowerDisabled, 0);

    mot_leftDriveRear_sparkmax_C4.follow(mot_leftDriveFront_sparkmax_C14);
    mot_leftDriveRear_sparkmax_C4.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_leftDriveRear_sparkmax_C4.burnFlash();

    mot_rightDriveFront_sparkmax_C15 = new CANSparkMax(Constants.DriveTrain.kRightDriveFront, MotorType.kBrushless);
    mot_rightDriveFront_sparkmax_C15.restoreFactoryDefaults();
    mot_rightDriveFront_sparkmax_C15.setIdleMode(Constants.DriveTrain.idle);
    mot_rightDriveFront_sparkmax_C15.setSmartCurrentLimit(60);
    mot_rightDriveFront_sparkmax_C15.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_rightDriveFront_sparkmax_C15.burnFlash();

    mot_rightDriveRear_sparkmax_C6 = new CANSparkMax(Constants.DriveTrain.kRightDriveRear, MotorType.kBrushless);
    mot_rightDriveRear_sparkmax_C6.restoreFactoryDefaults();
    mot_rightDriveRear_sparkmax_C6.setIdleMode(Constants.DriveTrain.idle);
    mot_rightDriveRear_sparkmax_C6.setSmartCurrentLimit(60);
    //follower fix
    mot_rightDriveFront_sparkmax_C15.follow(ExternalFollower.kFollowerDisabled, 0);

    mot_rightDriveRear_sparkmax_C6.follow(mot_rightDriveFront_sparkmax_C15);
    mot_rightDriveRear_sparkmax_C6.setOpenLoopRampRate(Constants.DriveTrain.loopRampRate);
    mot_rightDriveRear_sparkmax_C6.burnFlash();

    // Sets speed control group to the corisponding motor
    

    m_drive = new DifferentialDrive(mot_leftDriveFront_sparkmax_C14, mot_rightDriveFront_sparkmax_C15);

    m_leftEncoder = mot_leftDriveFront_sparkmax_C14.getEncoder();
    m_rightEncoder = mot_rightDriveFront_sparkmax_C15.getEncoder();

    // Sets the distance per pulse for the encoders
    // https://www.chiefdelphi.com/t/encoder-distance-per-pulse/156742

    m_leftEncoder.setPositionConversionFactor(Constants.DriveTrain.kEncoderDistancePerPulse);
    m_rightEncoder.setPositionConversionFactor(Constants.DriveTrain.kEncoderDistancePerPulse);

    m_leftEncoder.setVelocityConversionFactor(Constants.DriveTrain.kEncoderDistancePerPulse / 60);
    m_rightEncoder.setVelocityConversionFactor(Constants.DriveTrain.kEncoderDistancePerPulse / 60);

    resetEncoders();
    //m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
    
    /**
     * TODO: Make sure PneumaticsModuleType.CTREPCM is correct type
     * WPILib 2022 update
     * https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/DoubleSolenoid.html
     * https://first.wpi.edu/wpilib/allwpilib/docs/release/java/edu/wpi/first/wpilibj/PneumaticsModuleType.html
     */
    dsl_gearSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, Constants.DriveTrain.kShiftSolenoid1, Constants.DriveTrain.kShiftSolenoid2);

    // Calibrate the gyro
    m_navX = new AHRS(SPI.Port.kMXP);
    // Add NAVX calibration here
    
    // Set intial toggle value to false
    m_antiTipToggle = false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    // Update the odometry in the periodic block
    //m_odometry.update(Rotation2d.fromDegrees(getHeading()), m_leftEncoder.getPosition(), m_rightEncoder.getPosition());

    // Get the encoder positions and display in smart dashboard
    SmartDashboard.putNumber("kleftencoder value", m_leftEncoder.getVelocity()/* / Constants.DriveTrain.neo_encoder_position * 6 * Math.PI*/);
    SmartDashboard.putNumber("krightencoder value", m_rightEncoder.getVelocity()/* / Constants.DriveTrain.neo_encoder_position * 6 * Math.PI*/);
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
   * Method to get velocity of left encoder
   */
  public double getLeftEncoderRate() {
    return m_leftEncoder.getVelocity();
  }

  /**
   * Method to get velocity of right encoder
   */
  public double getRightEncoderRate() {
    return m_rightEncoder.getVelocity();
  }



  public void arcadeDrive(double fwd, double rot) {
    m_drive.arcadeDrive(fwd, rot);
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
 // public Pose2d getPose() {
  //  return m_odometry.getPoseMeters();
  //}

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
  //public void resetOdometry(Pose2d pose) {
    //resetEncoders();
   // m_odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
 // }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void manualDrive(final double acceleration, final double deceleration, final double turn) {
    final double accelerate = acceleration - deceleration;

    
    double turnOffset = 0;
    if(accelerate > 0.005){
      turnOffset = Constants.DriveTrain.turnOffset;
    } 
    else if(accelerate < -0.005){
      turnOffset = -Constants.DriveTrain.turnOffset;
    }

    m_drive.arcadeDrive(accelerate, turn + turnOffset, true);
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts  the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    mot_leftDriveFront_sparkmax_C14.setVoltage(leftVolts);
    mot_rightDriveFront_sparkmax_C15.setVoltage(-rightVolts);
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
  // CHANGED MIGHT BE FOR TRAJECTORY
  public double getAverageEncoderDistance() {
    return (m_leftEncoder.getPosition() * Constants.DriveTrain.distanceCalculate + m_rightEncoder.getPosition() * Constants.DriveTrain.distanceCalculate) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   * 
   * @return the left drive encoder
   */
  public RelativeEncoder getLeftEncoder() {
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
  public RelativeEncoder getRightEncoder() {
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
    mot_leftDriveFront_sparkmax_C14.set(speed);
  }

  public void setRightMotors(double speed) {
    mot_rightDriveFront_sparkmax_C15.set(speed);
  }

public double getAverageEncoderRate() {
	return (getLeftEncoderRate() + getRightEncoderRate())/2;
}

}
