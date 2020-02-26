/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import org.frc.team5409.robot.Constants.Indexer;
import org.frc.team5409.robot.subsystems.Limelight;
import org.frc.team5409.robot.subsystems.turret.ShooterFlywheel;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ShootAuto extends CommandBase {
  private ShooterFlywheel m_turret;
  private Indexer m_indexer;
  private Limelight m_limelight;

  /**
   * Creates a new ShootAuto.
   */
  public ShootAuto(ShooterFlywheel sys_flywheel, Indexer sys_indexer, Limelight sys_limelight) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_turret = sys_flywheel;
    m_indexer = sys_indexer;
    m_limelight = sys_limelight;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
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
    return false;
  }
}
