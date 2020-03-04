/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.Intake;
import org.frc.team5409.robot.util.Logger;

import java.time.Instant;

import org.frc.team5409.robot.subsystems.Indexer;

public class IntakeIndexActive extends CommandBase {

	// Time of Flight sensors
	boolean TOF_Enter;
	boolean TOF_Exit;
	boolean TOF_Ball1;

	protected boolean m_triggered;

	// Makes Indexer Motor run
	boolean indexerRun;

	// Whether or not ball is at position 1
	boolean ballAtPosition1;

	// detection of the number of power cells in indexer
	boolean IndexerFull;

	int powerCellsInIndexer;

	private final Intake subsys_Intake;
	private final Indexer subsys_indexer;

	/**
	 * Creates a new IntakeIndexActive
	 */
	public IntakeIndexActive(Indexer indexerSubsystem, Intake intakeSubsystem) {
		subsys_indexer = indexerSubsystem;
		subsys_Intake = intakeSubsystem;

		TOF_Enter = subsys_indexer.ballDetectionEnter();
		TOF_Ball1 = subsys_indexer.ballDetectionBall1();
		TOF_Exit = subsys_indexer.ballDetectionExit();
		
		addRequirements(indexerSubsystem, intakeSubsystem);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		if (!(subsys_indexer.ballDetectionExit() && subsys_indexer.isRangeValidExit())) {
			subsys_Intake.intakeOn(1);
			subsys_Intake.solenoidsDown();
		}
		
		//indexerRun = false;
		//ballAtPosition1 = false;

		// m_timer2 = Timer.getFPGATimestamp();
		// m_triggered = false;
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		TOF_Enter = subsys_indexer.ballDetectionEnter();
		TOF_Ball1 = subsys_indexer.ballDetectionBall1();

		SmartDashboard.putBoolean("TOF_Ball1", TOF_Ball1);
		SmartDashboard.putBoolean("TOF_Enter", TOF_Enter);
		SmartDashboard.putBoolean("TOF_Exit", TOF_Exit);
		// if time of flight sensor closest to the shooter is false run this

		// if statements to run the indexer motor
		if (TOF_Enter) {
			subsys_indexer.moveIndexerMotor(1);
		} else if (TOF_Ball1 && !TOF_Enter) {
			subsys_indexer.moveIndexerMotor(0);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		subsys_indexer.moveIndexerMotor(0);
		subsys_Intake.intakeOn(0);
		subsys_Intake.solenoidsUp();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return (subsys_indexer.ballDetectionExit() && subsys_indexer.isRangeValidExit());
	}

}