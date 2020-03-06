/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.playingwithfusion.TimeOfFlight;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc.team5409.robot.Constants;

/**
 * Add your docs here.
 * 
 * @param <neo550CanSparkMax>
 * @author Elizabeth
 * 
 * Subsystem that set up the indexer
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

	protected final CANSparkMax m_Indexer_neo550_C16;

	protected double m_output;

	// boolean to detect whether or not indexer is full
	boolean IndexerFull;

	public Indexer() {

		TOF_Enter = new TimeOfFlight(Constants.Indexer.TOF_Enter);
		TOF_Exit = new TimeOfFlight(Constants.Indexer.TOF_Exit);
		TOF_Ball1 = new TimeOfFlight(Constants.Indexer.TOF_Ball1); 

		m_Indexer_neo550_C16 = new CANSparkMax(Constants.Indexer.m_Indexer_neo550_C16, MotorType.kBrushless);
		m_Indexer_neo550_C16.setSmartCurrentLimit(Constants.Indexer.currentLimit);
		m_Indexer_neo550_C16.setIdleMode(IdleMode.kBrake);
		m_Indexer_neo550_C16.burnFlash();

		TOF_Enter.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Indexer.sampleTime);
		TOF_Exit.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Indexer.sampleTime);
		TOF_Ball1.setRangingMode(TimeOfFlight.RangingMode.Short, Constants.Indexer.sampleTime);

		m_output = 0;

		var parent = Shuffleboard.getTab("Robot Information")
								 .getLayout("Indexer Information", BuiltInLayouts.kList)
							     .getLayout("Indexer State", BuiltInLayouts.kGrid);
        	parent.addBoolean("Indexer Active", () -> { return m_output != 0; });
        	parent.addBoolean("Indexer Full", () -> { return ballDetectionExit(); });
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
		if (range < Constants.Indexer.rangeEnter_2) {

		// if (range > Constants.Indexer.rangeEnter_1 && range <
		// Constants.Indexer.rangeEnter_2) {

		return true;
		}
		return false;
	}

	// function that returns how many power cells are in the indexer
	// public int getNumberOfPowerCellsEnter() {
	// return getNumberOfPowerCellsEnter;
	// }

	// detects whether or not power cells are in range of ball1 sensor
	public boolean ballDetectionBall1() {
		double range = TOF_Ball1.getRange();
		if (range < Constants.Indexer.rangeBall1_2) {
		// if (range < Constants.Indexer.rangeBall1_1 && range >
		// Constants.Indexer.rangeBall1_2)
		
		return true;
		}
		return false;
	}

	// detects whether or not power cells are in range of exit sensor
	public boolean ballDetectionExit() {
		double range = TOF_Exit.getRange();
		if (range < Constants.Indexer.rangeExit_2) {

		// if (range < Constants.Indexer.rangeExit_1 && range >
		// Constants.Indexer.rangeExit_2) {

		return true;
		}
		return false;
	}

	/**
	 * methods that determine whether or not 
	 * the ranges of 
	 */
	public boolean isRangeValidEnter() {
		return TOF_Enter.isRangeValid();
	}

	public boolean isRangeValidExit() {
		return TOF_Exit.isRangeValid();
	}

	public boolean isRangeValidBall1() {
		return TOF_Ball1.isRangeValid();
	}

	// set ranging mode (short)
	public void setRangingMode(TimeOfFlight.RangingMode rangeModeIn, double sampleTime) {
		if (sampleTime > 24) { // Error Checking for sample time <24
			sampleTime = 24;
			TOF_Enter.setRangingMode(rangeModeIn, sampleTime);
		}
	}

	// sets up motor
	public void moveIndexerMotor(double output) {
		m_output = output;
		m_Indexer_neo550_C16.set(output);
	}

	@Override
	public void periodic() {
	}

}
