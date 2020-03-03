/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.IntakeandIndexer;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.subsystems.Indexer;

public class IndexerMove extends CommandBase {
	// Subsystem
	Indexer sys_indexer;

	// Variables
	boolean TOF_EnterRange;
	boolean TOF_ExitRange;

	/**
	 * Creates a new IntakeIndexActive
	 */
	public IndexerMove(Indexer subsystem) {
		sys_indexer = subsystem;
		addRequirements(sys_indexer);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		TOF_EnterRange = sys_indexer.getTOF_EnterRange() < 100;
		TOF_ExitRange = sys_indexer.getTOF_ExitRange() < 100;
		if (TOF_EnterRange && !TOF_ExitRange) {
			sys_indexer.setIndexerMotor(1);
		} else {
			sys_indexer.setIndexerMotor(0);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {
		sys_indexer.setIndexerMotor(0);
	}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		return false;
	}
}