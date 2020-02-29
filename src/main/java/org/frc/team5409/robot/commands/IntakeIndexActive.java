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

	protected Logger indexerLogger, indexerEvents;

	protected boolean m_triggered;

	// Makes Indexer Motor run
	boolean indexerRun;

	boolean intakeRun;

	// Whether or not ball is at position 1
	boolean ballAtPosition1;

	// detection of the number of power cells in indexer
	boolean IndexerFull;

	int powerCellsInIndexer;

	//private final Intake subsys_Intake;
	private final Indexer subsys_indexer;

	private double m_timer2;

	private double m_timer,
	  			   m_delay = 0.05;

	/**
	 * Creates a new IntakeIndexActive
	 */
	public IntakeIndexActive(Indexer indexerSubsystem) {
		subsys_indexer = indexerSubsystem;
		//subsys_Intake = intakeSubsystem;
		addRequirements(indexerSubsystem); //(intakeSubsystem);

		TOF_Enter = subsys_indexer.ballDetectionEnter();
		TOF_Ball1 = subsys_indexer.ballDetectionBall1();
		TOF_Exit = subsys_indexer.ballDetectionExit();

		powerCellsInIndexer = subsys_indexer.getNumberOfPowerCellsEnter();

	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		// subsys_Intake.extend();
		indexerRun = false;
		ballAtPosition1 = false;

		String logs_path = "indexer/"+Long.toString(Instant.now().getEpochSecond());
		indexerLogger = new Logger(logs_path+ "/INDEXER_DATA.csv");
		indexerEvents = new Logger(logs_path+ "/INDEXER_EVENTS.csv");

		m_timer2 = Timer.getFPGATimestamp();
		m_triggered = false;
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		double time = Timer.getFPGATimestamp()-m_timer2;

		TOF_Enter = subsys_indexer.ballDetectionEnter();
		TOF_Ball1 = subsys_indexer.ballDetectionBall1();
		TOF_Exit = subsys_indexer.ballDetectionExit();

		SmartDashboard.putBoolean("Ball at Position 1", ballAtPosition1);
		SmartDashboard.putBoolean("TOF_Enter", TOF_Enter);
		// if time of flight sensor closest to the shooter is false run this
		
		// if statements to run the indexer motor
		if (TOF_Exit) {
			subsys_indexer.moveIndexerMotor(0);
		} else if (TOF_Enter) {
			subsys_indexer.moveIndexerMotor(0.75);

			if (!m_triggered)
				indexerEvents.writeln("%f, INDEXER TRIGGERED", time);
			
			m_triggered = true;
		} else {
			if (m_triggered)
				m_timer = Timer.getFPGATimestamp();
			
			m_triggered = false;

			if (Timer.getFPGATimestamp()-m_timer > m_delay)
				subsys_indexer.moveIndexerMotor(0);
		}

		if (powerCellsInIndexer == 5) {
		// Retracts intake
		// subsys_Intake.retract();
		IndexerFull = true;
		SmartDashboard.putBoolean("Indexer Full", IndexerFull);
		powerCellsInIndexer = 0;
		}

		indexerLogger.writeln("%f, %f, %f, %f",
			time,
			subsys_indexer.getRangeEnter(),
			subsys_indexer.getRangeBall1(),
			subsys_indexer.getRangeExit()
		);
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		// subsys_Intake.retract();
		indexerLogger.save();
		indexerEvents.save();
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}