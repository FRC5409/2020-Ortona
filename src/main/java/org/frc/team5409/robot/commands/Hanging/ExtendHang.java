/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.Hanging;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.Hanging;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
/**
 * ExtendArmNeo Command
 * Runs neo to move while hang extends
 */
public class ExtendHang extends CommandBase {
  private Hanging sys_hanging;

  /**
   * Creates a new ExtendNeo.
   */
  public ExtendHang(Hanging subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    sys_hanging = subsystem;
    addRequirements(sys_hanging);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    sys_hanging.setPiston(Value.kReverse);
    sys_hanging.setHangMotor(0.6);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    sys_hanging.setHangMotor(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return sys_hanging.getEncoderAvgPosition() > 100000;
  }
}
