/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveStraightAuto extends CommandBase {
  DriveTrain subsystem;
  /**
   * Creates a new DriveStraightAuto.
   */
  public DriveStraightAuto(DriveTrain sub) {
    // Use addRequirements() here to declare subsystem dependencies.
    subsystem = sub;
    addRequirements(subsystem);
  
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    
    subsystem.resetEncoders();
    subsystem.setLeftMotors(0.2);
    subsystem.setRightMotors(0.2);

    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // if (subsystem.getAverageEncoderDistance() > 1) {
    //   subsystem.setLeftMotors(0);
    //   subsystem.setRightMotors(0);
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    subsystem.setRightMotors(0);
    subsystem.setLeftMotors(0);
    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    return false;
  }
}
