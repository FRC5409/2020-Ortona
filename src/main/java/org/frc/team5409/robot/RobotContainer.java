/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.frc.team5409.robot.commands.*;
import org.frc.team5409.robot.commands.DriveTrain.DriveCommand;
import org.frc.team5409.robot.commands.DriveTrain.FastGearShift;
import org.frc.team5409.robot.commands.DriveTrain.SlowGearShift;
import org.frc.team5409.robot.commands.Hanging.*;
import org.frc.team5409.robot.commands.IntakeandIndexer.IndexerMove;
import org.frc.team5409.robot.commands.IntakeandIndexer.IntakeForward;
import org.frc.team5409.robot.commands.IntakeandIndexer.IntakeReverse;
import org.frc.team5409.robot.commands.Trajectories.FirstPath;
import org.frc.team5409.robot.commands.Trajectories.firstPath;
import org.frc.team5409.robot.commands.shooter.logging.RunShooterFlywheel;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.subsystems.shooter.*;
import org.w3c.dom.CDATASection;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

	// Subsystems
	private final Indexer sys_indexer;
	private final Intake sys_intake;
	private final DriveTrain sys_driveTrain;
	private final Hanging sys_hanging;

	// Commands
	private final FirstPath cmd_firstPath;

	// Joystick Controllers
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
		cmd_firstPath = new FirstPath();
		//Joystick stuff
		joy_main = new XboxController(0);
		joy_secondary = new XboxController(1);

		// Drivetrain
		sys_driveTrain = new DriveTrain(); 

		// Hanging
		sys_hanging = new Hanging();

		// Shooter
		sys_shooter_turret = new ShooterTurret();
		sys_shooter_flywheel = new ShooterFlywheel();
		sys_limelight = new Limelight();

		// Indexer and Intake
		sys_indexer = new Indexer(); 
		sys_intake = new Intake();

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

		configureBindings();

		// Default Commands
		sys_driveTrain.setDefaultCommand(new DriveCommand(sys_driveTrain, joy_main));
		sys_limelight.setDefaultCommand(new RunShooterFlywheel(sys_shooter_flywheel, sys_indexer, sys_limelight, joy_main, joy_secondary));
		sys_indexer.setDefaultCommand(new IndexerMove(sys_indexer));
	}

	
	private void configureBindings() {
		// Intake
		but_main_A.whileHeld(new IntakeForward(sys_intake));
		but_main_B.whileHeld(new IntakeReverse(sys_intake));
		
		// Gear Shift
		// but_main_bmp_right.whenPressed(new FastGearShift(sys_driveTrain));
		// but_main_bmp_right.whenReleased(new SlowGearShift(sys_driveTrain));

		// Hang Control
		but_main_start.whenHeld(new ExtendHang(sys_hanging));
		but_main_back.whenHeld(new RetractHang(sys_hanging));
	}

	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(2), Units.feetToMeters(2));
		config.setKinematics(sys_driveTrain.getKinematics());
		String trajectoryJSON = "paths/firstPath.wpilib.json";
		Trajectory trajectory;
		try {
			Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
			trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
		} catch (IOException ex) {
			trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(new Pose2d()), config);
			DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
		}
		RamseteCommand ramseteCommand = new RamseteCommand(
			trajectory, 
			sys_driveTrain::getPose, 
			new RamseteController(), 
			sys_driveTrain.getFeedforward(), 
			sys_driveTrain.getKinematics(), 
			sys_driveTrain::getWheelSpeeds, 
			sys_driveTrain.getLeftPIDController(), 
			sys_driveTrain.getRightPIDController(), 
			sys_driveTrain::setOutputVoltage, 
			sys_driveTrain
		);
		return ramseteCommand.andThen(() -> sys_driveTrain.setOutputVoltage(0, 0));
	}

	public Command getOtherAutonomousCommand() {
		return cmd_firstPath;

	}
}
