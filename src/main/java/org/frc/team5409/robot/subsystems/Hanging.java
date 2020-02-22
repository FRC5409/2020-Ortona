/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANDigitalInput.LimitSwitchPolarity;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.frc.team5409.robot.Constants;

//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
/**
 * Hanging subsystem.
 * Contains all the methods needed for climb
 */
public class Hanging extends SubsystemBase {
  /**
   * Creates a new Hanging.
   */
  //Piston
  private DoubleSolenoid dsl_hangSolenoid;

  //Limit Switch
  // private static DigitalInput limitSwitch;

  //Neo
  private final CANSparkMax mot_hanging_neo_C3;
  public CANEncoder enc1_hanging;
  private final CANSparkMax mot_hanging_neo_C5;
  public CANEncoder enc2_hanging;
  private CANPIDController m_pidController_hanging;
  public double rotations;

  //Time Of Flight
  protected TimeOfFlight TOF_Hang;
  public double range_Hang;

  public Hanging() {
    // Piston
    dsl_hangSolenoid = new DoubleSolenoid(Constants.Hanging.FORWARD_CHANEL, Constants.Hanging.BACKWARD_CHANEL);

    // Neo
    mot_hanging_neo_C3 = new CANSparkMax(Constants.Hanging.NEO1_ID, MotorType.kBrushless);
    mot_hanging_neo_C5 = new CANSparkMax(Constants.Hanging.NEO2_ID, MotorType.kBrushless);

    mot_hanging_neo_C3.restoreFactoryDefaults();
    mot_hanging_neo_C5.restoreFactoryDefaults();

    mot_hanging_neo_C3.setSmartCurrentLimit(40);    
    mot_hanging_neo_C5.setSmartCurrentLimit(40);

    mot_hanging_neo_C3.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
    mot_hanging_neo_C3.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
    mot_hanging_neo_C5.getForwardLimitSwitch(LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);
    mot_hanging_neo_C5.getReverseLimitSwitch(LimitSwitchPolarity.kNormallyOpen).enableLimitSwitch(false);

    mot_hanging_neo_C3.setIdleMode(IdleMode.kBrake);
    mot_hanging_neo_C5.setIdleMode(IdleMode.kBrake);

    enc1_hanging = mot_hanging_neo_C3.getEncoder();
    enc2_hanging = mot_hanging_neo_C5.getEncoder();

    enc1_hanging.setPosition(0);
    enc2_hanging.setPosition(0); 

    mot_hanging_neo_C5.follow(mot_hanging_neo_C3, true);

    mot_hanging_neo_C3.burnFlash();
    mot_hanging_neo_C5.burnFlash();

    //Time Of Flight
    TOF_Hang = new TimeOfFlight(Constants.Hanging.TOF_ID);     
    TOF_Hang.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Hanging.TOF_SAMPLE_TIME);    

    setPid();

    // Limit Switch
    // limitSwitch = new DigitalInput(1);
  }

  public void setPid() {
    // Sets pid and displays on smart dashboard
    m_pidController_hanging = mot_hanging_neo_C3.getPIDController();
    m_pidController_hanging.setP(Constants.Hanging.kP);
    m_pidController_hanging.setI(Constants.Hanging.kI);
    m_pidController_hanging.setD(Constants.Hanging.kD);
    m_pidController_hanging.setIZone(Constants.Hanging.kIz);
    m_pidController_hanging.setFF(Constants.Hanging.kFF);
    m_pidController_hanging.setOutputRange(Constants.Hanging.kMinOutput, Constants.Hanging.kMaxOutput);
    SmartDashboard.putNumber("P Gain", Constants.Hanging.kP);
    SmartDashboard.putNumber("I Gain", Constants.Hanging.kI);
    SmartDashboard.putNumber("D Gain", Constants.Hanging.kD);
    SmartDashboard.putNumber("I Zone", Constants.Hanging.kIz);
    SmartDashboard.putNumber("Feed Forward", Constants.Hanging.kFF);
    SmartDashboard.putNumber("Max Output", Constants.Hanging.kMaxOutput);
    SmartDashboard.putNumber("Min Output", Constants.Hanging.kMinOutput);
    SmartDashboard.putNumber("Set Rotations", 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // adjustPid();
    range_Hang = TOF_Hang.getRange();
  }

  public void adjustPid() {
    double kP = Constants.Hanging.kP;
    double kI = Constants.Hanging.kI;
    double kD = Constants.Hanging.kD;
    double kIz = Constants.Hanging.kIz;
    double kFF = Constants.Hanging.kFF;
    double kMaxOutput = Constants.Hanging.kMaxOutput;
    double kMinOutput = Constants.Hanging.kMinOutput;

    // makes you able to input values on smart dashboard (pid configuration)
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    rotations = SmartDashboard.getNumber("Set Rotations", 0);

    if((p != kP)) { m_pidController_hanging.setP(p); kP = p; }
    if((i != kI)) { m_pidController_hanging.setI(i); kI = i; }
    if((d != kD)) { m_pidController_hanging.setD(d); kD = d; }
    if((iz != kIz)) { m_pidController_hanging.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { m_pidController_hanging.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) {
    m_pidController_hanging.setOutputRange(min, max);
    kMinOutput = min; kMaxOutput = max;
    }

    // set rotations
    m_pidController_hanging.setReference(rotations, ControlType.kPosition);

    // put new values on smart dashboard
    SmartDashboard.putNumber("SetPoint", rotations);
    SmartDashboard.putNumber("ProcessVariable", enc1_hanging.getPosition());
    SmartDashboard.putNumber("ProcessVariable", enc2_hanging.getPosition());
  }

/**
 * ExtendArmNeo Command
 * Runs neo to move while hang retracts
 */
  public void retractArmNeo() {
    rotations = Constants.Hanging.RETRACT_NEO_POS;
    m_pidController_hanging.setReference(rotations, ControlType.kPosition);
  }
/**
 * ExtendArmNeo 
 * Runs neo to move while hang extends
 */
  public void extendArmNeo() {
    rotations = Constants.Hanging.EXTEND_NEO_POS;
    m_pidController_hanging.setReference(rotations, ControlType.kPosition);
  }
/**
 * stopNeo
 * Stops the neo (sets the PID poisition to current position) once limit switch isSwitchSet
 */
  public void stopNeo() {
    // rotations = enc1_hanging.getPosition();
    // m_pidController_hanging.setReference(rotations, ControlType.kPosition);
    mot_hanging_neo_C3.stopMotor();
    mot_hanging_neo_C5.stopMotor();
  }

/**
 * isSwitchSet
 * Boolean that determines if the limit switch is set or not
 */
  // public static boolean isSwitchSet() {
  //   return !limitSwitch.get();
  // }

 /**
 * lockPiston
 * extends the piston to lock the hang
 */
  public void lockPiston() {
    dsl_hangSolenoid.set(Value.kForward);
  }
  /**
 * unlockPiston
 * unlocks the piston so the hang can shoot up
 */
  public void unlockPiston() {
    dsl_hangSolenoid.set(Value.kReverse);
  }
}
