/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot;

import org.frc.team5409.robot.commands.*;
import org.frc.team5409.robot.subsystems.*;
import edu.wpi.first.wpilibj.*;
import org.frc.team5409.robot.commands.turret.*;
import org.frc.team5409.robot.subsystems.turret.*;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class RobotContainer {

	private final TurretFlywheel sys_turret_flywheel;
	private final TurretRotation sys_turret_rotation;
	private final Indexer sys_Indexer;

	private final RunTurretFlywheel cmd_turret_run;
	private final IndexActive cmd_IndexActive;
	public final Intake sys_intakeSubsystem;
	public final IntakeActive m_intakeActive;

	private final SequentialCommandGroup grp_configure_turret;

	private final XboxController joy_main, joy_secondary;

	private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_sck_left, but_main_sck_right,
			but_main_bmp_left, but_main_bmp_right;

	private final JoystickButton but_secondary_A, but_secondary_B, but_secondary_X, but_secondary_Y,
			but_secondary_sck_left, but_secondary_sck_right, but_secondary_bmp_left, but_secondary_bmp_right;

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {

		// Liz's stuff
		sys_Indexer = new Indexer();

		cmd_IndexActive = new IndexActive(sys_Indexer);

		// Sanad's stuff
		sys_intakeSubsystem = new Intake();

		m_intakeActive = new IntakeActive(sys_intakeSubsystem);

		// Keith's stuff
		sys_turret_flywheel = new TurretFlywheel();
		sys_turret_rotation = new TurretRotation();

		joy_main = new XboxController(0);
		joy_secondary = new XboxController(1);

		cmd_turret_run = new RunTurretFlywheel(sys_turret_flywheel, sys_Indexer, joy_main, joy_secondary);

		grp_configure_turret = new SequentialCommandGroup(new ConfigureTurret(sys_turret_rotation, sys_turret_flywheel),
				new RotateTurret(sys_turret_rotation, 0));

		but_main_A = new JoystickButton(joy_main, XboxController.Button.kA.value);
		but_main_B = new JoystickButton(joy_main, XboxController.Button.kB.value);
		but_main_X = new JoystickButton(joy_main, XboxController.Button.kX.value);
		but_main_Y = new JoystickButton(joy_main, XboxController.Button.kY.value);
		but_main_sck_left = new JoystickButton(joy_main, XboxController.Button.kStickLeft.value);
		but_main_sck_right = new JoystickButton(joy_main, XboxController.Button.kStickRight.value);
		but_main_bmp_left = new JoystickButton(joy_main, XboxController.Button.kBumperLeft.value);
		but_main_bmp_right = new JoystickButton(joy_main, XboxController.Button.kBumperRight.value);

		but_secondary_A = new JoystickButton(joy_secondary, XboxController.Button.kA.value);
		but_secondary_B = new JoystickButton(joy_secondary, XboxController.Button.kB.value);
		but_secondary_X = new JoystickButton(joy_secondary, XboxController.Button.kX.value);
		but_secondary_Y = new JoystickButton(joy_secondary, XboxController.Button.kY.value);
		but_secondary_sck_left = new JoystickButton(joy_secondary, XboxController.Button.kStickLeft.value);
		but_secondary_sck_right = new JoystickButton(joy_secondary, XboxController.Button.kStickRight.value);
		but_secondary_bmp_left = new JoystickButton(joy_secondary, XboxController.Button.kBumperLeft.value);
		but_secondary_bmp_right = new JoystickButton(joy_secondary, XboxController.Button.kBumperRight.value);

		configureBindings();
	}

	private void configureBindings() {
		//but_main_A.whileActiveOnce(cmd_turret_run);
		//but_main_A.cancelWhenPressed(cmd_IndexActive);
		sys_turret_flywheel.setDefaultCommand(cmd_turret_run);

		//but_main_X.toggleWhenPressed(cmd_IndexActive);
	}
}
