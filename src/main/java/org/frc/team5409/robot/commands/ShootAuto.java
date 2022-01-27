/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import org.frc.team5409.robot.commands.shooter.*;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.subsystems.shooter.*;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShootAuto extends SequentialCommandGroup {;
  /**
   * Creates a new shootAuto.
   */
  public ShootAuto(ShooterFlywheel sys_flywheel, 
                   ShooterTurret sys_rotation, 
                   Limelight sys_limelight, 
                   Indexer sys_indexer,
                   DriveTrain sys_driveTrain,
                   Intake sys_intake
                  ) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
      new OperateShooter(sys_flywheel, sys_rotation, sys_limelight, sys_indexer).withTimeout(3.5),
      new ParallelCommandGroup(
        new DriveStraightAuto(sys_driveTrain, 0.75, 0.75),
        new RotateTurret(sys_rotation, 0),
        new IntakeIndexActive(sys_indexer, sys_intake)
      ).withTimeout(3),
      new DriveStraightAuto(sys_driveTrain, -0.8, -0.8).withTimeout(2.5),
      new DriveStraightAuto(sys_driveTrain, 0.1, 0.1).withTimeout(1),
      new OperateShooter(sys_flywheel, sys_rotation, sys_limelight, sys_indexer).withTimeout(6)
    );
    //addCommand();
    //addCommand();
  }
}
