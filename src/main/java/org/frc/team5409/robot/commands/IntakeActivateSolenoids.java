/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import org.frc.team5409.robot.subsystems.Intake;

public class IntakeActivateSolenoids extends CommandBase {
  /**
   * Creates a new IntakeActivateSolenoids.
   * 
   * Command that runs the intake solenoids
   */

  private final Intake subsys_Intake;

  public IntakeActivateSolenoids(Intake intakeSubsystem) {
    
    subsys_Intake = intakeSubsystem; 

    addRequirements(intakeSubsystem);
    
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    if(subsys_Intake.isExtended()){
      subsys_Intake.solenoidsDown();
    } else {
      subsys_Intake.solenoidsUp();
    }

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
