/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hanging extends SubsystemBase {
  /**
   * Creates a new Hanging.
   */
  //Piston
  private DoubleSolenoid dsl_hangSolenoid;

  //Limit Switch
  private static DigitalInput limitSwitch;

  //Neo
  private final CANSparkMax mot_hanging_neo_C3;
  public CANEncoder enc_xxxx_hanging;
  private final CANSparkMax mot_hanging_neo_C5;
  public CANEncoder encoder2;
  private CANPIDController m_pidController_hanging;
  private double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  public double rotations;

  public Hanging() {
    // Piston
    dsl_hangSolenoid = new DoubleSolenoid(Constants.DS1_ID, Constants.DS2_ID, Constants.DS3_ID);

    // Neo
    mot_hanging_neo_C3 = new CANSparkMax(Constants.NEO1_ID, MotorType.kBrushless);
    mot_hanging_neo_C5 = new CANSparkMax(Constants.NEO2_ID, MotorType.kBrushless);

    enc_xxxx_hanging = mot_hanging_neo_C3.getEncoder();

    mot_hanging_neo_C3.setIdleMode(IdleMode.kBrake);
    mot_hanging_neo_C5.setIdleMode(IdleMode.kBrake);

    mot_hanging_neo_C3.restoreFactoryDefaults();
    mot_hanging_neo_C3.setSmartCurrentLimit(40);
    mot_hanging_neo_C5.restoreFactoryDefaults();
    mot_hanging_neo_C5.setSmartCurrentLimit(40);

    mot_hanging_neo_C5.follow(mot_hanging_neo_C3, true);
    enc_xxxx_hanging.setPosition(0);

    // encoder2 = mot_hanging_neo_C3.getEncoder();

    setPid();

    // Limit Switch
    limitSwitch = new DigitalInput(1);
  }

  public void setPid() {
    // Sets pid and displays on smart dashboard
    m_pidController_hanging = mot_hanging_neo_C3.getPIDController();
    kP = 0.1;
    kI = 1e-4;
    kD = 1;
    kIz = 0;
    kFF = 0;
    kMaxOutput = 0.1;
    kMinOutput = -0.1;
    m_pidController_hanging.setP(kP);
    m_pidController_hanging.setI(kI);
    m_pidController_hanging.setD(kD);
    m_pidController_hanging.setIZone(kIz);
    m_pidController_hanging.setFF(kFF);
    m_pidController_hanging.setOutputRange(kMinOutput, kMaxOutput);
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);
    SmartDashboard.putNumber("Set Rotations", 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    adjustPid();
  }

  public void adjustPid() {
    // makes you able to input values on smart dashboard (pid configuration)
    // double p = SmartDashboard.getNumber("P Gain", 0);
    // double i = SmartDashboard.getNumber("I Gain", 0);
    // double d = SmartDashboard.getNumber("D Gain", 0);
    // double iz = SmartDashboard.getNumber("I Zone", 0);
    // double ff = SmartDashboard.getNumber("Feed Forward", 0);
    // double max = SmartDashboard.getNumber("Max Output", 0);
    // double min = SmartDashboard.getNumber("Min Output", 0);
    // rotations = SmartDashboard.getNumber("Set Rotations", 0);

    // if((p != kP)) { m_pidController.setP(p); kP = p; }
    // if((i != kI)) { m_pidController.setI(i); kI = i; }
    // if((d != kD)) { m_pidController.setD(d); kD = d; }
    // if((iz != kIz)) { m_pidController.setIZone(iz); kIz = iz; }
    // if((ff != kFF)) { m_pidController.setFF(ff); kFF = ff; }
    // if((max != kMaxOutput) || (min != kMinOutput)) {
    // m_pidController.setOutputRange(min, max);
    // kMinOutput = min; kMaxOutput = max;
    // }

    // set rotations
    m_pidController_hanging.setReference(rotations, ControlType.kPosition);

    // put new values on smart dashboard
    SmartDashboard.putNumber("SetPoint", rotations);
    SmartDashboard.putNumber("ProcessVariable", enc_xxxx_hanging.getPosition());
  }

  // Neo
  public void retractNeo() {
    rotations = Constants.RETRACT_NEO_POS;
  }

  public void extendNeo() {
    rotations = Constants.EXTEND_NEO_POS;
  }

  public void endNeo() {
    // mot_hanging_neo_C3.set(0);
    rotations = enc_xxxx_hanging.getPosition();
  }

  // Limit Switch
  public static boolean isSwitchSet() {
    return !limitSwitch.get();
  }

  // Piston
  public void extendPiston() {
    dsl_hangSolenoid.set(Value.kForward);
  }

  public void retractPiston() {
    dsl_hangSolenoid.set(Value.kReverse);
  }
}
