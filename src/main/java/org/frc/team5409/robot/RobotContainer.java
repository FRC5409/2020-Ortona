/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot;

import java.util.List;

import org.frc.team5409.robot.commands.*;
import org.frc.team5409.robot.commands.Hanging.ExtendHangMax;
import org.frc.team5409.robot.commands.Hanging.RetractHang;
import org.frc.team5409.robot.commands.autonomous.ComplexAuto;
import org.frc.team5409.robot.commands.autonomous.SimpleAutoBackward;
import org.frc.team5409.robot.commands.autonomous.SimpleAutoForward;
import org.frc.team5409.robot.commands.shooter.*;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.subsystems.shooter.*;
import org.frc.team5409.robot.util.*;
import org.frc.team5409.robot.util.AutoCommand.AutonomousState;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

	// subsystems
	// private final ShooterFlywheel subsys_shooter_flywheel;
	// private final ShooterTurret subsys_shooter_turret;
	private final Indexer sys_indexer;
	public final Intake sys_intake;
	public final DriveTrain sys_driveTrain;
	private final Hanging sys_hang;

	// commands
	private IntakeIndexActive cmd_IntakeIndexActive;
	private AntiTipToggle cmd_AntiTipToggle;
	private FastGearShift cmd_FastGearShift;
	private SlowGearShift cmd_SlowGearShift;
	public final DriveCommand cmd_drive;
	public DriveStraightAuto cmd_DriveStraightAuto;
	public final IndexerReverse cmd_IndexerReverse;
	private final IntakeActivateSolenoids cmd_IntakeActivateSolenoids;

	// joystick controllers
	private final XboxController joy_main, joy_secondary;

	private final ShooterTurret sys_shooter_turret;
	private final ShooterFlywheel sys_shooter_flywheel;

	private final Limelight sys_limelight;

	private SendableChooser<Command> auto_command;

	// buttons
	private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_sck_left, but_main_sck_right,
			but_main_bmp_left, but_main_bmp_right, but_main_start, but_main_back;

	private final JoystickButton but_secondary_A, but_secondary_B, but_secondary_X, but_secondary_Y,
			but_secondary_sck_left, but_secondary_sck_right, but_secondary_bmp_left, but_secondary_bmp_right,
			but_secondary_start, but_secondary_back;;

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {
		// Joystick stuff
		joy_main = new XboxController(0);
		joy_secondary = new XboxController(1);
		sys_driveTrain = new DriveTrain();

		// WuTang's stuff
		sys_hang = new Hanging();

		cmd_drive = new DriveCommand(sys_driveTrain, joy_main);
		// cmd_DriveStraightAuto = new DriveStraightAuto(sys_driveTrain);

		sys_shooter_turret = new ShooterTurret();
		sys_shooter_flywheel = new ShooterFlywheel();

		sys_limelight = new Limelight();

		// Liz's stuff
		sys_indexer = new Indexer();
		cmd_IndexerReverse = new IndexerReverse(sys_indexer);

		// Sanad's stuff
		sys_intake = new Intake();
		cmd_IntakeActivateSolenoids = new IntakeActivateSolenoids(sys_intake);

		// Keith's stuff
		// subsys_shooter_flywheel = new ShooterFlywheel();
		// subsys_shooter_turret = new ShooterTurret();

		cmd_IntakeIndexActive = new IntakeIndexActive(sys_indexer, sys_intake);

		// grp_configure_turret = new SequentialCommandGroup(new
		// CalibrateShooter(subsys_shooter_turret, subsys_shooter_flywheel),
		// new RotateTurret(subsys_shooter_turret, 0));

		// Buttons
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
		but_secondary_start = new JoystickButton(joy_secondary, XboxController.Button.kStart.value);
		but_secondary_back = new JoystickButton(joy_secondary, XboxController.Button.kBack.value);

		// configureBindings();

		sys_driveTrain.setDefaultCommand(new DriveCommand(sys_driveTrain, joy_main));

		/*
		 * sys_indexer.setDefaultCommand(new IntakeIndexActive(sys_indexer,
		 * subsys_intake)); but_main_A.whenPressed( new SequentialCommandGroup( new
		 * CalibrateShooter(sys_shooter_turret, sys_shooter_flywheel), new
		 * RotateTurret(sys_shooter_turret, 0) ) );
		 */

		// but_main_Y.whileActiveOnce(new SmoothSweep(sys_shooter_turret,
		// sys_shooter_flywheel));
		but_main_A
				.whileActiveOnce(
						new OperateShooter(sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer))
				.whenInactive(new RotateTurret(sys_shooter_turret, 0));
		// sys_shooter_flywheel.setDefaultCommand(new
		// RunShooterFlywheel(sys_shooter_flywheel, sys_indexer, sys_limelight,
		// joy_main, joy_secondary));
		but_main_B.whileActiveOnce(new ReverseIntake(sys_intake));
		// sys_indexer.setDefaultCommand(new IntakeIndexActive(sys_indexer,
		// subsys_intake));
		configureBindings();
		configureDashboard();
	}

	private void configureBindings() {
		// but_main_A.whileActiveOnce(cmd_turret_run);
		// but_main_A.cancelWhenPressed(cmd_IndexActive);

		// Run intake while held
		but_main_Y.whileHeld(new IntakeIndexActive(sys_indexer, sys_intake));

		but_main_X.whenPressed(new IntakeActivateSolenoids(sys_intake));

		// Reverse intake while held
		but_main_B.whileHeld(new ReverseIntake(sys_intake));

		// Toggle AntiTip
		but_secondary_Y.whenPressed(new AntiTipToggle(sys_driveTrain));

		// Shift gear to fast
		but_main_bmp_right.whenPressed(new FastGearShift(sys_driveTrain));
		// Shift gear to slow
		but_main_bmp_right.whenReleased(new SlowGearShift(sys_driveTrain));

		but_main_bmp_left.whileHeld(new IndexerReverse(sys_indexer));

		// climb

		but_secondary_back.whileHeld(new RetractHang(sys_hang));
		but_secondary_start.whenPressed(new ExtendHangMax(sys_hang));
		but_main_back.whileHeld(new RetractHang(sys_hang));
		but_main_start.whenPressed(new ExtendHangMax(sys_hang));

		but_secondary_bmp_right.whenPressed(new OffsetFlywheel(sys_shooter_flywheel, true));
		but_secondary_bmp_left.whenPressed(new OffsetFlywheel(sys_shooter_flywheel, false));

		but_secondary_X.whenPressed(new ResetFlywheelOffset(sys_shooter_flywheel));
	}

	public void configureDashboard() {
		auto_command = new SendableChooser<>();

		auto_command.setDefaultOption("Simple Auto Backward", new SimpleAutoBackward(
			sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer, sys_driveTrain
		));
		auto_command.addOption("Simple Auto Forward", new SimpleAutoForward(
			sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer, sys_driveTrain
		));
		auto_command.addOption("Complex Auto", new ComplexAuto(
			sys_shooter_flywheel, sys_shooter_turret, sys_limelight,
			sys_indexer, sys_driveTrain, sys_intake
		));

		var robot_config = Shuffleboard.getTab("Robot Configuration");
			var auto_config = robot_config.getLayout("Autonomous Configuration", BuiltInLayouts.kList);
				//auto_config.withPosition(0, 0);
				//auto_config.withSize(2, 4);
					auto_config.add("Autonomous State", auto_command);

		var robot_info = Shuffleboard.getTab("Robot Information");
			var auto_info = robot_info.getLayout("Autonomous Information", BuiltInLayouts.kList)
									  .withPosition(0, 0)
									  .withSize(2, 2);
				var auto_state = auto_info.getLayout("Autonomous State", BuiltInLayouts.kGrid);
					auto_state.addBoolean("Shooting State", () -> { return ((AutoCommand) auto_command.getSelected()).getState(AutonomousState.kShooting); });
					auto_state.addBoolean("Driving State", () -> { return ((AutoCommand) auto_command.getSelected()).getState(AutonomousState.kDriving); });
			
					auto_state.addBoolean("Intaking State", () -> { return ((AutoCommand) auto_command.getSelected()).getState(AutonomousState.kIntaking); });
				auto_info.addBoolean("Autonomous Finished", () -> { return ((AutoCommand) auto_command.getSelected()).getState(AutonomousState.kFinished); });
			
			var shooter_info = robot_info.getLayout("Shooter Information", BuiltInLayouts.kList)
									     .withPosition(7, 0)
									     .withSize(2, 4);
				shooter_info.addNumber("Flywheel Velocity", sys_shooter_flywheel::getVelocity)
							.withWidget(BuiltInWidgets.kDial);
				shooter_info.addNumber("Turret Rotation", sys_shooter_turret::getRotation)
							.withWidget(BuiltInWidgets.kGyro);

				var target_info = shooter_info.getLayout("Shooter Target Information", BuiltInLayouts.kGrid);
					target_info.addNumber("Velocity Target", sys_shooter_flywheel::getTarget)
							   .withPosition(0, 0);
					target_info.addNumber("Rotation Target", sys_shooter_turret::getTarget)
							   .withPosition(1, 0);
					target_info.addBoolean("Vel. Reached", sys_shooter_flywheel::isTargetReached)
							   .withPosition(0, 1);
					target_info.addBoolean("Rot. Reached", sys_shooter_turret::isTargetReached)
							   .withPosition(1, 1);

			var general_info = robot_info.getLayout("General Information", BuiltInLayouts.kList)
										 .withPosition(5,0)
										 .withSize(2, 2);
				general_info.addNumber("Velocity Offset", sys_shooter_flywheel::getVelocityOffset);

			var indexer_info = robot_info.getLayout("Indexer Information", BuiltInLayouts.kList);
										 //.withPosition(8, 3)
										 //.withSize(5, 10);
				var indexer_state = indexer_info.getLayout("Indexer State", BuiltInLayouts.kGrid);
					indexer_state.addBoolean("Indexer Active", sys_indexer::isActive)
								 .withPosition(0, 0);
					indexer_state.addBoolean("Indexer Full", sys_indexer::ballDetectionExit)
								 .withPosition(1, 0);
			
			var intake_info = robot_info.getLayout("Intake Information", BuiltInLayouts.kList);
										//.withPosition(14, 3)
										//.withSize(5, 5);
				intake_info.addBoolean("Intake Jammed", sys_intake::isIntakeJammed);
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
//';'
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
		//return new ShootAuto(sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer, sys_driveTrain, sys_intake);
		//return new shootAuto();
		return auto_command.getSelected();
	}
}
