// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class SetAntiTip extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive;
  private final boolean applyAntiTip;

  /**
   * Creates a new SetAntiTip.
   *
   * @param subsystem The subsystem used by this command.
   */
  public SetAntiTip(DriveTrain subsystem, boolean _applyAntiTip) {
    drive = subsystem;
    applyAntiTip = _applyAntiTip;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drive.setAntiTip(applyAntiTip);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
