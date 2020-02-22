/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import org.frc.team5409.robot.Constants;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
  private CANSparkMax mot_intake_sparkMax_C10;
  private DoubleSolenoid d_rightIntakeSolenoid;
  private DoubleSolenoid d_leftIntakeSolenoid;

  /**
   * Creates a new Intake.
   */
  public Intake() {
    //mot_intake_sparkMax_C10 = new CANSparkMax(Constants.Intake.kIntakeMotor, MotorType.kBrushless);
    //d_rightIntakeSolenoid = new DoubleSolenoid(Constants.Intake.kRightIntakeSolenoid1, Constants.Intake.kRightIntakeSolenoid2);
    //d_leftIntakeSolenoid = new DoubleSolenoid(Constants.Intake.kLeftIntakeSolenoid1, Constants.Intake.kLeftIntakeSolenoid2);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  /**
   * Method to extend intake
   */
  public void extend() {
    d_rightIntakeSolenoid.set(DoubleSolenoid.Value.kForward);
    d_leftIntakeSolenoid.set(DoubleSolenoid.Value.kForward);
    mot_intake_sparkMax_C10.set(0.5);
  }

  /**
   * Method to retract intake
   */
  public void retract() {
    d_rightIntakeSolenoid.set(DoubleSolenoid.Value.kReverse);
    d_leftIntakeSolenoid.set(DoubleSolenoid.Value.kReverse);
    mot_intake_sparkMax_C10.set(0);
  }
  
  public void reverse() {
    mot_intake_sparkMax_C10.set(-0.5);
  }
}
