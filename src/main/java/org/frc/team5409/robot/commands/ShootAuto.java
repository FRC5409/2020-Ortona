// /*----------------------------------------------------------------------------*/
// /* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
// /* Open Source Software - may be modified and shared by FRC teams. The code   */
// /* must be accompanied by the FIRST BSD license file in the root directory of */
// /* the project.                                                               */
// /*----------------------------------------------------------------------------*/

// package org.frc.team5409.robot.commands;

<<<<<<< HEAD
// import edu.wpi.first.wpilibj2.command.CommandBase;

// import org.frc.team5409.robot.commands.shooter.OperateShooter;
// import org.frc.team5409.robot.subsystems.Indexer;
// import org.frc.team5409.robot.subsystems.Limelight;
// import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;
// import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;
// public class ShootAuto extends CommandBase {
// 	private OperateShooter cmd_OperateShooter;
//   ShooterFlywheel shooterflywheel;
//   ShooterTurret shooterturret;
//   Limelight limelight;
//   Indexer indexer;
  
//   /**
//    * Creates a new ShootAuto.
//    */
//   public ShootAuto(ShooterFlywheel sys_flywheel, ShooterTurret sys_rotation, Limelight sys_limelight, Indexer sys_indexer) {
//     shooterflywheel=sys_flywheel;
//     shooterturret=sys_rotation;
//     limelight= sys_limelight;
//     indexer= sys_indexer;

//     cmd_OperateShooter = new OperateShooter( shooterflywheel, shooterturret, limelight, indexer);

//     // Use addRequirements() here to declare subsystem dependencies.
//     addRequirements(shooterflywheel,shooterturret,limelight, indexer);

    
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
=======
import org.frc.team5409.robot.RobotContainer;
import org.frc.team5409.robot.commands.shooter.OperateShooter;
import org.frc.team5409.robot.subsystems.DriveTrain;
import org.frc.team5409.robot.subsystems.Indexer;
import org.frc.team5409.robot.subsystems.Limelight;
import org.frc.team5409.robot.subsystems.shooter.ShooterFlywheel;
import org.frc.team5409.robot.subsystems.shooter.ShooterTurret;
>>>>>>> Karina and Aadil's work on auto

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

<<<<<<< HEAD
//   }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
//     cmd_OperateShooter( shooterflywheel, shooterturret, limelight, indexer);
//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     return false;
//   }
// }
=======
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class shootAuto extends SequentialCommandGroup {;
  /**
   * Creates a new shootAuto.
   */
  public shootAuto(ShooterFlywheel sys_flywheel, 
                   ShooterTurret sys_rotation, 
                   Limelight sys_limelight, 
                   Indexer sys_indexer,
                   DriveTrain sys_driveTrain
                  ) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(new OperateShooter(sys_flywheel, sys_rotation, sys_limelight, sys_indexer),
          new DriveStraightAuto(sys_driveTrain));
    //addCommand();
    //addCommand();
  }
}
>>>>>>> Karina and Aadil's work on auto
