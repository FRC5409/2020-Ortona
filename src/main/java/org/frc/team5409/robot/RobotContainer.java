/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot;

import java.io.IOException;

import org.frc.team5409.robot.commands.*;
import org.frc.team5409.robot.commands.autonomous.ComplexAuto;
import org.frc.team5409.robot.commands.autonomous.SimpleAutoBackward;
import org.frc.team5409.robot.commands.autonomous.SimpleAutoForward;
import org.frc.team5409.robot.commands.shooter.*;
import org.frc.team5409.robot.commands.trainer.BranchTargetSetpoint;
import org.frc.team5409.robot.commands.trainer.FlipTargetSetpoint;
import org.frc.team5409.robot.commands.trainer.RequestModelUpdate;
import org.frc.team5409.robot.commands.trainer.SubmitSetpointData;
import org.frc.team5409.robot.commands.trainer.TrainerAlignTurret;
import org.frc.team5409.robot.commands.trainer.TrainerOperateShooter;
import org.frc.team5409.robot.commands.trainer.UndoTargetSetpoint;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.subsystems.shooter.*;
import org.frc.team5409.robot.training.protocol.NetworkClient;
import org.frc.team5409.robot.training.protocol.NetworkSocket;
import org.frc.team5409.robot.training.protocol.SendableContext;
import org.frc.team5409.robot.training.protocol.generic.KeyValueSendable;
import org.frc.team5409.robot.training.protocol.generic.StringSendable;
import org.frc.team5409.robot.training.protocol.generic.ValueSendable;
import org.frc.team5409.robot.training.robot.Range;
import org.frc.team5409.robot.training.robot.Setpoint;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;
import org.frc.team5409.robot.util.*;
import org.frc.team5409.robot.util.AutoCommand.AutonomousState;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

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

	private TrainingContext training_context;
	private TrainerDashboard trainer_dashboard;
	private NetworkClient trainer_client;

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

		sys_shooter_turret = new ShooterTurret();
		sys_shooter_flywheel = new ShooterFlywheel();

		sys_limelight = new Limelight();

		// Liz's stuff
		sys_indexer = new Indexer();
		cmd_IndexerReverse = new IndexerReverse(sys_indexer);

		// Sanad's stuff
		sys_intake = new Intake();
		cmd_IntakeActivateSolenoids = new IntakeActivateSolenoids(sys_intake);

		cmd_IntakeIndexActive = new IntakeIndexActive(sys_indexer, sys_intake);

		// Buttons
		but_main_A = new JoystickButton(joy_main, XboxController.Button.kA.value);
		but_main_B = new JoystickButton(joy_main, XboxController.Button.kB.value);
		but_main_X = new JoystickButton(joy_main, XboxController.Button.kX.value);
		but_main_Y = new JoystickButton(joy_main, XboxController.Button.kY.value);
		but_main_sck_left = new JoystickButton(joy_main, XboxController.Button.kLeftStick.value);
		but_main_sck_right = new JoystickButton(joy_main, XboxController.Button.kRightStick.value);
		but_main_bmp_left = new JoystickButton(joy_main, XboxController.Button.kLeftBumper.value);
		but_main_bmp_right = new JoystickButton(joy_main, XboxController.Button.kLeftBumper.value);
		but_main_start = new JoystickButton(joy_main, XboxController.Button.kStart.value);
		but_main_back = new JoystickButton(joy_main, XboxController.Button.kBack.value);

		but_secondary_A = new JoystickButton(joy_secondary, XboxController.Button.kA.value);
		but_secondary_B = new JoystickButton(joy_secondary, XboxController.Button.kB.value);
		but_secondary_X = new JoystickButton(joy_secondary, XboxController.Button.kX.value);
		but_secondary_Y = new JoystickButton(joy_secondary, XboxController.Button.kY.value);
		but_secondary_sck_left = new JoystickButton(joy_secondary, XboxController.Button.kLeftStick.value);
		but_secondary_sck_right = new JoystickButton(joy_secondary, XboxController.Button.kRightStick.value);
		but_secondary_bmp_left = new JoystickButton(joy_secondary, XboxController.Button.kLeftBumper.value);
		but_secondary_bmp_right = new JoystickButton(joy_secondary, XboxController.Button.kLeftBumper.value);
		but_secondary_start = new JoystickButton(joy_secondary, XboxController.Button.kStart.value);
		but_secondary_back = new JoystickButton(joy_secondary, XboxController.Button.kBack.value);

		sys_driveTrain.setDefaultCommand(new DriveCommand(sys_driveTrain, joy_main));

		try {
			configureTraining();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		configureBindings();
		configureDashboard();
	}

	private void configureBindings() {
		but_main_A.whileActiveOnce(new TrainerOperateShooter(training_context, sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer))
			      .whenInactive(new RotateTurret(sys_shooter_turret, 0));
		
		but_main_B.whileActiveOnce(new ReverseIntake(sys_intake));

		// but_main_A.whileActiveOnce(cmd_turret_run);
		// but_main_A.cancelWhenPressed(cmd_IndexActive);

		// Run intake while held
		but_main_Y.whileHeld(new IntakeIndexActive(sys_indexer, sys_intake));

		but_main_X.whenPressed(new IntakeActivateSolenoids(sys_intake));

		// Reverse intake while held
		but_main_B.whileHeld(new ReverseIntake(sys_intake));
		
		but_main_back.whenPressed(
			new SequentialCommandGroup(	
				new CalibrateTurret(sys_shooter_turret),
				new RotateTurret(sys_shooter_turret, 0)
			)	
		);

		// Toggle AntiTip
		//but_secondary_Y.whenPressed(new AntiTipToggle(sys_driveTrain));

		// Shift gear to fast
		but_main_bmp_right.whenPressed(new FastGearShift(sys_driveTrain));
		// Shift gear to slow
		but_main_bmp_right.whenReleased(new SlowGearShift(sys_driveTrain));

		but_main_bmp_left.whileHeld(new IndexerReverse(sys_indexer));

		but_main_start.whenPressed(new CalibrateTurret(sys_shooter_turret));
		
		but_secondary_X.whenPressed(
			new BranchTargetSetpoint(trainer_dashboard, training_context, true)
		);

		but_secondary_B.whenPressed(
			new BranchTargetSetpoint(trainer_dashboard, training_context, false)
		);

		but_secondary_Y.whenPressed(
			new FlipTargetSetpoint(trainer_dashboard, training_context)
		);

		but_secondary_start.whenPressed(
			new SubmitSetpointData(trainer_dashboard, trainer_client, training_context)
		);

		but_secondary_back.whenPressed(
			new UndoTargetSetpoint(trainer_dashboard, training_context)
		);

		but_secondary_bmp_left.whenPressed(
			new RequestModelUpdate(trainer_client, training_context)
		);

		but_secondary_A.whileHeld(
			new TrainerAlignTurret(trainer_dashboard, training_context, sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer)
		);

		but_secondary_A.whenReleased(
			new RotateTurret(sys_shooter_turret, 0)
		);
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

	public Command getAutonomousCommand() {
		//return new ShootAuto(sys_shooter_flywheel, sys_shooter_turret, sys_limelight, sys_indexer, sys_driveTrain, sys_intake);
		//return new shootAuto();
		return auto_command.getSelected();
	}

	public void configureTraining() throws IOException {
		SendableContext context = new SendableContext();
              context.registerSendable(ValueSendable.class);
              context.registerSendable(KeyValueSendable.class);
              context.registerSendable(StringSendable.class);
			  
		NetworkSocket socket = NetworkSocket.create(
			Constants.Training.TRAINER_HOSTNAME);

		trainer_client = new NetworkClient(socket, context);

		training_context = new TrainingContext(
			new Setpoint(1000, new Range(0, 6000))
		);

		trainer_dashboard = new TrainerDashboard(training_context);
	}
}
