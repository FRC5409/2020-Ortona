/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc.team5409.robot.Constants;

/**
 * Add your docs here.
 * 
 * @param <neo550CanSparkMax>
 * @author Elizabeth
 * 
 */
public class Indexer extends SubsystemBase {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  // TOF Sensors
  protected TimeOfFlight TOF_Enter;
  protected TimeOfFlight TOF_Exit;

  // get the valid ranges of all three sensors
  protected boolean isRangeValidEnter;
  protected boolean isRangeValidExit;

  protected final CANSparkMax mot_indexer;

  public Indexer() {

    TOF_Enter = new TimeOfFlight(Constants.Indexer.TOF_Enter);
    TOF_Enter.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Indexer.sampleTime);

    TOF_Exit = new TimeOfFlight(Constants.Indexer.TOF_Exit);
    TOF_Exit.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Indexer.sampleTime);

    mot_indexer = new CANSparkMax(Constants.Indexer.m_indexer_id, MotorType.kBrushless);
    mot_indexer.setSmartCurrentLimit(Constants.Indexer.currentLimit);
    mot_indexer.setIdleMode(IdleMode.kBrake);
    mot_indexer.burnFlash();
  }

  // sets up motor
  public void setIndexerMotor(double speed) {
    mot_indexer.set(speed);
  }

  // Get TOF_Enter Range
  public double getTOF_EnterRange() {
    return TOF_Enter.getRange();
  }

  // Get TOF_Enter Range
  public double getTOF_ExitRange() {
    return TOF_Enter.getRange();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Range of EnterSensor", TOF_Enter.getRange());
    SmartDashboard.putNumber("Range of ExitSensor", TOF_Exit.getRange());

    SmartDashboard.putBoolean("Is the range of the EnterSensor valid?", TOF_Enter.isRangeValid());
    SmartDashboard.putBoolean("Is the range of the ExitSensor valid?", TOF_Exit.isRangeValid());
  }

}
