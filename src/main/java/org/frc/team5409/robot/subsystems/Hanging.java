/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.frc.team5409.robot.Constants;

//import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
/**
 * Hanging subsystem.
 * Contains all the methods needed for climb
 */
public class Hanging extends SubsystemBase {
  /**
   * Creates a new Hanging.
   */

  // Double Solenoid
  private DoubleSolenoid dsl_hangSolenoid;

  // Motors
  private CANSparkMax mot_hangingSlave;
  private CANSparkMax mot_hangingMaster;

  // Encoders
  public CANEncoder m_slaveEncoder;
  public CANEncoder m_masterEncoder;

  public Hanging() {
    // Double Solenoid
    dsl_hangSolenoid = new DoubleSolenoid(Constants.Hanging.kForwardChannel, Constants.Hanging.kBackwardChannel);

    // Master Motor
    mot_hangingMaster = new CANSparkMax(Constants.Hanging.kID_motorMaster, MotorType.kBrushless);
    mot_hangingMaster.restoreFactoryDefaults();
    mot_hangingMaster.setSmartCurrentLimit(40);
    mot_hangingMaster.setIdleMode(IdleMode.kCoast);
    mot_hangingMaster.burnFlash();
    
    // Slave Motor
    mot_hangingSlave = new CANSparkMax(Constants.Hanging.kID_motorSlave, MotorType.kBrushless);
    mot_hangingSlave.restoreFactoryDefaults();
    mot_hangingSlave.setSmartCurrentLimit(40);
    mot_hangingSlave.setIdleMode(IdleMode.kCoast);
    mot_hangingSlave.burnFlash();
    mot_hangingSlave.setInverted(true);

    mot_hangingSlave.follow(mot_hangingMaster);

    m_slaveEncoder = mot_hangingSlave.getEncoder();
    m_masterEncoder = mot_hangingMaster.getEncoder();
    resetEncoders();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public double getEncoderAvgPosition() {
    return (m_slaveEncoder.getPosition() + m_masterEncoder.getPosition()) / 2.0;
  }

  public void setHangMotor(double speed) {
    mot_hangingMaster.set(speed);
  }

  public void setPiston(Value value) {
    dsl_hangSolenoid.set(value);
  }

  public void resetEncoders() {
    m_slaveEncoder.setPosition(0);
    m_masterEncoder.setPosition(0);
  }

}
