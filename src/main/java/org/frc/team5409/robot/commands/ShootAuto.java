/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;
<<<<<<< HEAD

import org.frc.team5409.robot.RobotContainer;
import org.frc.team5409.robot.commands.shooter.OperateShooter;
import org.frc.team5409.robot.subsystems.DriveTrain;
import org.frc.team5409.robot.subsystems.Indexer;
import org.frc.team5409.robot.subsystems.Limelight;
import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;
import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;
=======
>>>>>>> fixed shoot auto

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
<<<<<<< HEAD
public class ShootAuto extends SequentialCommandGroup {;
=======
public class auto extends SequentialCommandGroup {
>>>>>>> fixed shoot auto
  /**
   * Creates a new auto.
   */
<<<<<<< HEAD
  public ShootAuto(ShooterFlywheel sys_flywheel, 
                   ShooterTurret sys_rotation, 
                   Limelight sys_limelight, 
                   Indexer sys_indexer,
                   DriveTrain sys_driveTrain
                  ) {
=======
  public auto() {
>>>>>>> fixed shoot auto
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
  }
}
