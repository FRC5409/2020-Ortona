/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import org.frc.team5409.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SlowGearShift extends CommandBase {
  private final DriveTrain sys_driveSubsystem;

  /**
   * Creates a new SlowGearShift.
   */
  public SlowGearShift(DriveTrain subsystem) {
    sys_driveSubsystem = subsystem; 
    addRequirements(sys_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Shifts to slow gear
    sys_driveSubsystem.slowShift();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
