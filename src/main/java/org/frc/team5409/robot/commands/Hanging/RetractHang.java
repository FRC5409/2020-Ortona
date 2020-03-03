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
 * RetractArmNeo Command
 * Runs neo to move while hang retracts
 */
public class RetractHang extends CommandBase {
  /**
   * Creates a new RetractNeo.
   */
  private final Hanging m_hanging;

  public RetractHang(Hanging subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_hanging = subsystem;
    addRequirements(m_hanging);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_hanging.setPiston(Value.kForward);
    m_hanging.controlArmNeo(-0.8);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_hanging.controlArmNeo(0);
  }


  // Returns true when the command should end.
  @Override
  public boolean isFinished() {

    // if (m_hanging.range_Hang < Constants.Hanging.TOF_RANGE){
    //   return = true;
    // }
    // if (m_hanging.isSwitchSet()) {
    //   finished = true;
    // }
    return m_hanging.getEncoderAvgPosition() < Constants.Hanging.RETRACT_NEO_POS;
  }
}
