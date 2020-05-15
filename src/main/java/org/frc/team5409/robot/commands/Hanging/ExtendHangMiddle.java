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
public class ExtendHangMiddle extends CommandBase {
  /**
   * Creates a new ExtendNeo.
   */
  private final Hanging m_hanging;

  public ExtendHangMiddle(Hanging subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_hanging = subsystem;
    addRequirements(m_hanging);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_hanging.setMotorsCoast();
    m_hanging.setPiston(Value.kReverse);
    m_hanging.controlArmNeo(-0.8);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_hanging.controlArmNeo(0.7);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_hanging.controlArmNeo(0);
    m_hanging.setPiston(Value.kForward);    
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    // if(m_hanging.isSwitchSet() == false){
    // finished = true;
    // }
    return m_hanging.getEncoderAvgPosition() > Constants.Hanging.EXTEND_NEO_POS_MIDDLE;
  }
}
