/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.subsystems.DriveTrain;

public class AntiTipToggle extends CommandBase {
  private DriveTrain sys_driveSubsystem;

  /**
   * Creates a new AntiTipToggle.
   */
  public AntiTipToggle(DriveTrain subsystem) {
    sys_driveSubsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(sys_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Toggle between true and false
    if (sys_driveSubsystem.getAntiTip()) {
      sys_driveSubsystem.turnAntiTipOff();
    } else {
      sys_driveSubsystem.turnAntiTipOn();
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