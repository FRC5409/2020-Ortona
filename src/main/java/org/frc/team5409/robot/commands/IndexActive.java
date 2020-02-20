/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.commands.IndexActive;
import org.frc.team5409.robot.subsystems.Indexer;

public class IndexActive extends CommandBase {

	// Time of Flight sensors
	boolean TOF_Enter;
	boolean TOF_Exit;
	boolean TOF_Ball1;

	// Makes Indexer Motor run
	boolean indexerRun;

	// Whether or not ball is at position 1
	boolean ballAtPosition1;

	// detection of the number of power cells in indexer
	boolean IndexerFull;

	int powerCellsInIndexer; 

	private final Indexer m_indexer;


	/**
	 * Creates a new Indexer.
	 */
	public IndexActive(Indexer Subsystem) {
		m_indexer = Subsystem;
		addRequirements(Subsystem);

		TOF_Enter = m_indexer.ballDetectionEnter();
		TOF_Ball1 = m_indexer.ballDetectionBall1();
		TOF_Exit = m_indexer.ballDetectionExit();

		powerCellsInIndexer = m_indexer.numberOfPowerCellsEnter(); 

	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		indexerRun = false;
		ballAtPosition1 = false;
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {

		TOF_Enter = m_indexer.ballDetectionEnter();
		TOF_Ball1 = m_indexer.ballDetectionBall1();
		TOF_Exit = m_indexer.ballDetectionExit();

		SmartDashboard.putBoolean("Ball at Position 1", ballAtPosition1);
		SmartDashboard.putBoolean("TOF_Enter", TOF_Enter); 
		// if time of flight sensor closest to the shooter is false run this
		if (TOF_Exit == false) {
			// time of flight closest to intake becomes true and the indexer will run
			if (TOF_Enter == true) {
				indexerRun = true;
			}

			// if ball1 (sensor in the middle) is true then ball at position 1 is true
			if (TOF_Ball1 == true) {
				ballAtPosition1 = true;
			}

			// if enter sensor is false but ball at position one is true then indexer will
			// stop
			if (TOF_Enter == false && ballAtPosition1 == true) {
				indexerRun = false;
				// else if they are both true than run until ball at position 1 is false
			} else if (TOF_Enter == true && ballAtPosition1 == true) {
				indexerRun = true;
				ballAtPosition1 = false;
			}

		} else {
			// if time of flight sensor exit becomes true stop the indexer
			indexerRun = false;
		}

		// if statements to run the indexer motor
		if (indexerRun == true) {
			m_indexer.moveIndexerMotor(0.8);
		} else {
			m_indexer.moveIndexerMotor(0);
		}

        if (powerCellsInIndexer == 5){
			IndexerFull = true;
			SmartDashboard.putBoolean("Indexer Full", IndexerFull); 
			powerCellsInIndexer = 0; 
			//stop intake motor
		}

	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {

	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}