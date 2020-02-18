/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

//import com.playingwithfusion.TimeOfFlight.Status; 

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

  // sensor at entrance
  protected TimeOfFlight TOF_Enter;
  // sensor at exit
  protected TimeOfFlight TOF_Exit;
  // sensor to detect balls
  protected TimeOfFlight TOF_Ball1;

  // get the valid ranges of all three sensors
  protected boolean isRangeValidEnter;
  protected boolean isRangeValidExit;
  protected boolean isRangeValidBall1;

  // getting ranges of all three sensors
  protected double getRangeEnter;
  protected double getRangeExit;
  protected double getRangeBall1;

  // number of power cells going through each sensor
  public int numberOfPowerCellsEnter;

  // detection whether or not ball has passed through sensor
  public boolean ballDetectionEnter;

  public boolean ballDetectionBall1;

  public boolean ballDetectionExit;

  // get ranging modes of all three sensors
  protected TimeOfFlight.RangingMode getRangingModeEnter;
  protected TimeOfFlight.RangingMode getRangingModeExit;
  protected TimeOfFlight.RangingMode getRangingModeBall1;

  protected final CANSparkMax m_Indexer_neo550_C16;

  // boolean to detect whether or not indexer is full
  boolean IndexerFull;

  public Indexer() {

    TOF_Enter = new TimeOfFlight(Constants.Indexer.TOF_Enter);
    TOF_Exit = new TimeOfFlight(Constants.Indexer.TOF_Exit);
    TOF_Ball1 = new TimeOfFlight(Constants.Indexer.TOF_Ball1);

    numberOfPowerCellsEnter = 0;

    m_Indexer_neo550_C16 = new CANSparkMax(Constants.Indexer.m_Indexer_neo550_C16, MotorType.kBrushless);
    m_Indexer_neo550_C16.setSmartCurrentLimit(Constants.Indexer.currentLimit);
    m_Indexer_neo550_C16.burnFlash();
    m_Indexer_neo550_C16.setIdleMode(IdleMode.kBrake);

    TOF_Enter.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Indexer.sampleTime);
    TOF_Exit.setRangingMode(TimeOfFlight.RangingMode.Short, 24);
    TOF_Ball1.setRangingMode(TimeOfFlight.RangingMode.Short, 24);

  }

  // the measured distance in mm
  public double getRangeEnter() {
    return TOF_Enter.getRange();
  }

  public double getRangeExit() {
    return TOF_Exit.getRange();
  }

  public double getRangeBall1() {
    return TOF_Ball1.getRange();
  }

  // ball detection functions
  public boolean ballDetectionEnter() {
    double range = TOF_Enter.getRange();
    if (range < 125 && range > 50
    ) {

      // if (range == 115) {
      //   numberOfPowerCellsEnter++;
      // }

      return true;
    }
    return false;
  }

  public int numberOfPowerCellsEnter(){
	  return numberOfPowerCellsEnter; 
  }

  public boolean ballDetectionBall1() {
    double range = TOF_Ball1.getRange();
    if (range < 130 && range > 100) {
      return true;
    }
    return false;
  }

  // Note: test range for exit sensor
  public boolean ballDetectionExit() {
    double range = TOF_Exit.getRange();
    if (range < 150 && range > 100) {
      return true;
    }
    return false;
  }

  // determines whether or not range is valid
  public boolean isRangeValidEnter() {
    return TOF_Enter.isRangeValid();
  }

  public boolean isRangeValidExit() {
    return TOF_Exit.isRangeValid();
  }

  public boolean isRangeValidBall1() {
    return TOF_Ball1.isRangeValid();
  }

  // getting ranging mode (short, medium, long)
  public TimeOfFlight.RangingMode getRangingModeEnter() {
    return TOF_Enter.getRangingMode();
  }

  public TimeOfFlight.RangingMode getRangingModeExit() {
    return TOF_Exit.getRangingMode();
  }

  public TimeOfFlight.RangingMode getRangingModeBall1() {
    return TOF_Ball1.getRangingMode();
  }

  // set ranging mode (short)
  public void setRangingMode(TimeOfFlight.RangingMode rangeModeIn, double sampleTime) {
    if (sampleTime == 24) { // Error Checking for sample time <24
      TOF_Enter.setRangingMode(rangeModeIn, sampleTime);
    } else if (sampleTime > 24) {
    }
  }

  public void moveIndexerMotor(double output) {
    m_Indexer_neo550_C16.set(output);
  }
  // make a stop indexer motor

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Range of EnterSensor", TOF_Enter.getRange());
    SmartDashboard.putNumber("Range of ExitSensor", TOF_Exit.getRange());
    SmartDashboard.putNumber("Range of Ball1", TOF_Ball1.getRange());

    SmartDashboard.putBoolean("Is the range of the EnterSensor valid?", TOF_Enter.isRangeValid());
    SmartDashboard.putBoolean("Is the range of the ExitSensor valid?", TOF_Exit.isRangeValid());
    SmartDashboard.putBoolean("Is the range of the Ball1Sensor valid?", TOF_Ball1.isRangeValid());

    SmartDashboard.putNumber("Number of Power Cells", numberOfPowerCellsEnter);
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand())

  }

}
