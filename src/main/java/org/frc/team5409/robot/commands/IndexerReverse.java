/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.Indexer;

import org.frc.team5409.robot.subsystems.Intake;

public class IndexerReverse extends CommandBase {
  /**
   * Creates a new IndexerReverse.
   */

   private final Indexer subsys_indexer;
   private final Intake subsys_intake;

  public IndexerReverse(Indexer indexerSubsystem, Intake intakeSubsystem) {
    subsys_indexer = indexerSubsystem;
    subsys_intake = intakeSubsystem;

    addRequirements(subsys_indexer, subsys_intake); 
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    subsys_indexer.moveIndexerMotor(-0.5);

    subsys_intake.solenoidsUp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    subsys_indexer.moveIndexerMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    subsys_intake.solenoidsDown();
    
    return false;
  }
}
