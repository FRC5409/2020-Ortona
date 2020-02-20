/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.Hanging;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.Hanging;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ExtendArmNeo extends CommandBase {
  /**
   * Creates a new ExtendNeo.
   */
  private final Hanging m_hanging;

  public ExtendArmNeo(Hanging subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_hanging = subsystem;
    addRequirements(m_hanging);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_hanging.extendArmNeo();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  public boolean finished = false;

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if ((m_hanging.enc1_hanging.getPosition() + m_hanging.enc2_hanging.getPosition())/2 >= Constants.Hanging.EXTEND_NEO_POS) {
      finished = true;
    }
    // if(m_hanging.isSwitchSet() == false){
    // finished = true;
    // }
    return finished;
  }
}
