/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.Hanging;

import org.frc.team5409.robot.subsystems.Hanging;

import edu.wpi.first.wpilibj2.command.CommandBase;
/**
 * LocksPiston Command
 * Extends piston so that it locks hang in place after retraction
 */
public class LockPiston extends CommandBase {
  /**
   * Creates a new ExtendPiston.
   */
  private final Hanging m_hanging;

  public LockPiston(Hanging subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_hanging = subsystem;
    addRequirements(m_hanging);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_hanging.lockPiston();
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
