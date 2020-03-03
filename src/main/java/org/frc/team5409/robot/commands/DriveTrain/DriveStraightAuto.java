/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.DriveTrain;
import org.frc.team5409.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveStraightAuto extends CommandBase {
  DriveTrain sys_driveTrain;
  /**
   * Creates a new DriveStraightAuto.
   */
  public DriveStraightAuto(DriveTrain subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    sys_driveTrain = subsystem;
    addRequirements(sys_driveTrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    sys_driveTrain.resetEncoders();
    sys_driveTrain.setLeftMotors(0.5);
    sys_driveTrain.setRightMotors(0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sys_driveTrain.setRightMotors(0);
    sys_driveTrain.setLeftMotors(0);
  }


  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return sys_driveTrain.getAvgEncoderDistance() > 2;
  }
}
