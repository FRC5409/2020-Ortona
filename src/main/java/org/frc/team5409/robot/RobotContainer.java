/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot;

import java.util.List;

import org.frc.team5409.robot.commands.*;
import org.frc.team5409.robot.commands.shooter.*;
import org.frc.team5409.robot.commands.shooter.RotateTurret;
import org.frc.team5409.robot.commands.shooter.logging.RunShooterFlywheel;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.subsystems.shooter.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

	//subsystems
	//private final ShooterFlywheel subsys_shooter_flywheel;
	//private final ShooterTurret subsys_shooter_turret;
	private final Indexer sys_indexer;
	public final Intake subsys_intake;
	public final DriveTrain subsys_driveTrain;
	
	//commands
	private IntakeIndexActive cmd_IntakeIndexActive;
	private AntiTipToggle cmd_AntiTipToggle;
	private FastGearShift cmd_FastGearShift;
	private SlowGearShift cmd_SlowGearShift;
	public final DriveCommand cmd_drive;
	public final DriveStraightAuto cmd_DriveStraightAuto;

	// public final RetractArmNeo cmd_RetractArmNeo;
	// public final ExtendArmNeo cmd_ExtendArmNeo;
	// public final UnlockPiston cmd_UnlockPiston;
	// public final LockPiston cmd_LockPiston;
	// public final Retract cmd_Retract;
	// public final Extend cmd_Extend;

	//joystick controllers
	private final XboxController joy_main, joy_secondary;

	private final ShooterTurret sys_shooter_turret;
	private final ShooterFlywheel sys_shooter_flywheel;

	private final Limelight sys_limelight;

	//buttons
	private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_sck_left, but_main_sck_right,
			but_main_bmp_left, but_main_bmp_right,but_main_start,but_main_back;

	private final JoystickButton but_secondary_A, but_secondary_B, but_secondary_X, but_secondary_Y,
			but_secondary_sck_left, but_secondary_sck_right, but_secondary_bmp_left, but_secondary_bmp_right;

	

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		//Joystick stuff
		joy_main = new XboxController(0);
		joy_secondary = new XboxController(1);
		subsys_driveTrain = new DriveTrain(); 

		//WuTang's stuff
		cmd_drive = new DriveCommand(subsys_driveTrain, joy_main);
		cmd_DriveStraightAuto = new DriveStraightAuto(subsys_driveTrain);
		// cmd_RetractArmNeo = new RetractArmNeo(subsys_climb);
		// cmd_ExtendArmNeo = new ExtendArmNeo(subsys_climb);
		// cmd_UnlockPiston = new UnlockPiston(subsys_climb);
		// cmd_LockPiston = new LockPiston(subsys_climb);
		// cmd_Retract = new Retract(subsys_climb);
		// cmd_Extend = new Extend(subsys_climb);

		sys_shooter_turret = new ShooterTurret();
		sys_shooter_flywheel = new ShooterFlywheel();

		sys_limelight = new Limelight();

		// Liz's stuff
		sys_indexer = new Indexer(); 

		// Sanad's stuff
		subsys_intake = new Intake();
		
		// Keith's stuff
		//subsys_shooter_flywheel = new ShooterFlywheel();
		//subsys_shooter_turret = new ShooterTurret();

		cmd_IntakeIndexActive = new IntakeIndexActive(sys_indexer); //(subsys_intake);

		//grp_configure_turret = new SequentialCommandGroup(new CalibrateShooter(subsys_shooter_turret, subsys_shooter_flywheel),
		//		new RotateTurret(subsys_shooter_turret, 0));

		//Buttons
		but_main_A = new JoystickButton(joy_main, XboxController.Button.kA.value);
		but_main_B = new JoystickButton(joy_main, XboxController.Button.kB.value);
		but_main_X = new JoystickButton(joy_main, XboxController.Button.kX.value);
		but_main_Y = new JoystickButton(joy_main, XboxController.Button.kY.value);

		but_main_sck_left = new JoystickButton(joy_main, XboxController.Button.kStickLeft.value);
		but_main_sck_right = new JoystickButton(joy_main, XboxController.Button.kStickRight.value);
		but_main_bmp_left = new JoystickButton(joy_main, XboxController.Button.kBumperLeft.value);
		but_main_bmp_right = new JoystickButton(joy_main, XboxController.Button.kBumperRight.value);
		but_main_start = new JoystickButton(joy_main, XboxController.Button.kStart.value);
		but_main_back = new JoystickButton(joy_main, XboxController.Button.kBack.value);
	

		but_secondary_A = new JoystickButton(joy_secondary, XboxController.Button.kA.value);
		but_secondary_B = new JoystickButton(joy_secondary, XboxController.Button.kB.value);
		but_secondary_X = new JoystickButton(joy_secondary, XboxController.Button.kX.value);
		but_secondary_Y = new JoystickButton(joy_secondary, XboxController.Button.kY.value);
		but_secondary_sck_left = new JoystickButton(joy_secondary, XboxController.Button.kStickLeft.value);
		but_secondary_sck_right = new JoystickButton(joy_secondary, XboxController.Button.kStickRight.value);
		but_secondary_bmp_left = new JoystickButton(joy_secondary, XboxController.Button.kBumperLeft.value);
		but_secondary_bmp_right = new JoystickButton(joy_secondary, XboxController.Button.kBumperRight.value);

		//configureBindings();


		subsys_driveTrain.setDefaultCommand(new DriveCommand(subsys_driveTrain, joy_main));

		/*sys_indexer.setDefaultCommand(new IntakeIndexActive(sys_indexer, subsys_intake));
		but_main_A.whenPressed(
			new SequentialCommandGroup(
				new CalibrateShooter(sys_shooter_turret, sys_shooter_flywheel),
				new RotateTurret(sys_shooter_turret, 0)
			)		
		);*/

		//but_main_Y.whileActiveOnce(new SmoothSweep(sys_shooter_turret, sys_shooter_flywheel));
		but_main_Y.whileActiveOnce(new OperateShooter(sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer))
			      .whenInactive(new RotateTurret(sys_shooter_turret, 0));
		//sys_shooter_flywheel.setDefaultCommand(new RunShooterFlywheel(sys_shooter_flywheel, sys_indexer, sys_limelight, joy_main, joy_secondary));
		//but_main_B.whileActiveOnce(new ReverseIndexer(sys_indexer));
		sys_indexer.setDefaultCommand(new IntakeIndexActive(sys_indexer));
		//configureBindings();
	}

	
	private void configureBindings() {
		//but_main_A.whileActiveOnce(cmd_turret_run);
		//but_main_A.cancelWhenPressed(cmd_IndexActive);

	    // Run intake while held
		//but_main_Y.whileHeld(new IntakeIndexActive(subsys_indexer, subsys_intake));
	    but_secondary_Y.whenPressed(new AntiTipToggle(subsys_driveTrain));
		
		// Shift gear to fast
		but_main_bmp_right.whenPressed(new FastGearShift(subsys_driveTrain));
		// Shift gear to slow
		but_main_bmp_right.whenReleased(new SlowGearShift(subsys_driveTrain));
	}

	  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
//   public Command getAutonomousCommand() {

//     // Create a voltage constraint to ensure we don't accelerate too fast
//     final var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
//         new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.kvVoltSecondsPerMeter,
//             Constants.Trajectory.kaVoltSecondsSquaredPerMeter),
//         Constants.Trajectory.kDriveKinematics, 10);

//     // Create config for trajectory
//     final TrajectoryConfig config = new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond,
//         Constants.Trajectory.kMaxAccelerationMetersPerSecondSquare)

//             // Add kinematics to ensure max speed is actually obeyed
//             .setKinematics(Constants.Trajectory.kDriveKinematics)

//             // Apply the voltage constraint
//             .addConstraint(autoVoltageConstraint);

//     // An example trajectory to follow. All units in meters.
//     final Trajectory trajectory = TrajectoryGenerator.generateTrajectory(

//         // Start at the origin facing the +X direction
//         new Pose2d(0, 0, new Rotation2d(0)),

//         // Pass through these two interior waypoints, making an 's' curve path
//         List.of(new Translation2d(1, 1), new Translation2d(2, -1)),

//         // End 3 meters straight ahead of where we started, facing forward
//         new Pose2d(3, 0, new Rotation2d(0)),

//         // Pass config
//         config);

//     final RamseteCommand ramseteCommand = new RamseteCommand( 

//         trajectory, subsys_driveTrain::getPose, new RamseteController(Constants.Trajectory.kRamseteB, Constants.Trajectory.kRamseteZeta),

//         new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.kvVoltSecondsPerMeter,

//             Constants.Trajectory.kaVoltSecondsSquaredPerMeter),

//         Constants.Trajectory.kDriveKinematics, subsys_driveTrain::getWheelSpeeds, new PIDController(Constants.Trajectory.kPDriveVel, 0, 0),

//         new PIDController(Constants.Trajectory.kPDriveVel, 0, 0),

//         // RamseteCommand passes volts to the callback
//         subsys_driveTrain::tankDriveVolts, subsys_driveTrain);

//     // Run path following command, then stop at the end.
//     return ramseteCommand.andThen(() -> subsys_driveTrain.tankDriveVolts(0, 0));
//   }

	public Command getAutonomousCommand() {
		return cmd_DriveStraightAuto;
	}
}
