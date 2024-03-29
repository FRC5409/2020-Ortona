/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import org.frc.team5409.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ReverseIntake extends CommandBase {
  private final Intake subsys_Intake;

  /**
   * Creates a new ReverseIntake.
   */
  public ReverseIntake(Intake intakeSubsystem) {
    subsys_Intake = intakeSubsystem;
		addRequirements(intakeSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    subsys_Intake.intakeOn(-1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    subsys_Intake.intakeOn(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
