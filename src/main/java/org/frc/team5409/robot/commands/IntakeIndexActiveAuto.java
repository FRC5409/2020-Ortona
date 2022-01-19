/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.Intake;
import org.frc.team5409.robot.subsystems.Indexer;

public class IntakeIndexActiveAuto extends CommandBase {

	// Time of Flight sensors
	boolean TOF_Enter;
	boolean TOF_Exit;
	boolean TOF_Ball1;

	protected boolean m_triggered;

	// Makes Indexer Motor run
	boolean indexerRun;

	private final Intake subsys_Intake;
	private final Indexer subsys_indexer;

	/**
	 * Creates a new IntakeIndexActive
	 * 
	 * Command to run the indexer
	 */
	public IntakeIndexActiveAuto(Indexer indexerSubsystem, Intake intakeSubsystem) {
		subsys_indexer = indexerSubsystem;
		subsys_Intake = intakeSubsystem;
		
		addRequirements(indexerSubsystem, intakeSubsystem);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {

		//safety to stop running the intake when the indexer is full
		if (!(subsys_indexer.ballDetectionExit() && subsys_indexer.isRangeValidExit())) {
			subsys_Intake.intakeOn(1);
			subsys_Intake.solenoidsDown();
		}
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		TOF_Enter = subsys_indexer.ballDetectionEnter();
		TOF_Ball1 = subsys_indexer.ballDetectionBall1();
		TOF_Exit = subsys_indexer.ballDetectionExit();

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

		if(subsys_indexer.ballDetectionExit()){

			subsys_Intake.solenoidsUp();
			
		}
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		//return (subsys_indexer.ballDetectionExit() && subsys_indexer.isRangeValidExit());
		return false;
	}

}