/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.Hanging;

import org.frc.team5409.robot.subsystems.Hanging;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ExtendHangMiddleSeq extends SequentialCommandGroup {
  /**
   * Creates a new ExtendHangHighSeq.
   */
  public ExtendHangMiddleSeq(Hanging m_hanging) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new RetractHang(m_hanging).withTimeout(0.1),
          new ExtendHangMiddle(m_hanging));
  }
}
