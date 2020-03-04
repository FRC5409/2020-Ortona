/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import org.frc.team5409.robot.RobotContainer;
import org.frc.team5409.robot.commands.shooter.OperateShooter;
import org.frc.team5409.robot.subsystems.DriveTrain;
import org.frc.team5409.robot.subsystems.Indexer;
import org.frc.team5409.robot.subsystems.Limelight;
import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;
import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;

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
                   DriveTrain sys_driveTrain
                  ) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new OperateShooter(sys_flywheel, sys_rotation, sys_limelight, sys_indexer).withTimeout(6),
          new DriveStraightAuto(sys_driveTrain).withTimeout(2));
    //addCommand();
    //addCommand();
  }
}
