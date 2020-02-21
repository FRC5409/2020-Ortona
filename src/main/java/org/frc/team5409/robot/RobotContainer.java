/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot;

import java.util.List;

import org.frc.team5409.robot.commands.*;
import org.frc.team5409.robot.commands.Hanging.Extend;
import org.frc.team5409.robot.commands.Hanging.ExtendArmNeo;
import org.frc.team5409.robot.commands.Hanging.LockPiston;
import org.frc.team5409.robot.commands.Hanging.Retract;
import org.frc.team5409.robot.commands.Hanging.RetractArmNeo;
import org.frc.team5409.robot.commands.Hanging.UnlockPiston;
import org.frc.team5409.robot.subsystems.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;

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
	public final DriveTrain sys_driveTrain;
	private final SequentialCommandGroup grp_configure_turret;
	private final AntiTipToggle m_AntiTipToggle;
	private final XboxController joy_main, joy_secondary;
	public final DriveCommand m_driveCommand = new DriveCommand(sys_driveTrain, joystick);
	private final JoystickButton but_main_A, but_main_B, but_main_X;
	public final static JoystickButton but_main_Y;
	private final JoystickButton but_main_sck_left;
	private final JoystickButton but_main_sck_right;
	private final JoystickButton but_main_bmp_left;
	private final JoystickButton but_main_bmp_right;
	private final JoystickButton but_main_start;
	private final JoystickButton but_main_back;

	private final JoystickButton but_secondary_A, but_secondary_B, but_secondary_X, but_secondary_Y,
			but_secondary_sck_left, but_secondary_sck_right, but_secondary_bmp_left, but_secondary_bmp_right;

	public final Hanging m_hanging;
	public final RetractArmNeo cmd_RetractArmNeo;
	public final ExtendArmNeo cmd_ExtendArmNeo;
	public final UnlockPiston cmd_UnlockPiston;
	public final LockPiston cmd_LockPiston;
	public final Retract cmd_Retract;
	public final Extend cmd_Extend;

	/**
	 * The container for the robot. Contains subsystems, OI devices, and commands.
	 */
	public RobotContainer() {

		//WuTang's stuff
		m_hanging = new Hanging();
		cmd_RetractArmNeo = new RetractArmNeo(m_hanging);
		cmd_ExtendArmNeo = new ExtendArmNeo(m_hanging);
		cmd_UnlockPiston = new UnlockPiston(m_hanging);
		cmd_LockPiston = new LockPiston(m_hanging);
		cmd_Retract = new Retract(m_hanging);
		cmd_Extend = new Extend(m_hanging);

		// Liz's stuff
		sys_Indexer = new Indexer();

		cmd_IndexActive = new IndexActive(sys_Indexer, sys_intakeSubsystem);

		// Sanad's stuff
		sys_intakeSubsystem = new Intake();
		
		m_AntiTipToggle= new AntiTipToggle(sys_driveTrain);
		m_intakeActive = new IntakeActive(sys_intakeSubsystem);

		// Keith's stuff
		sys_turret_flywheel = new TurretFlywheel();
		sys_turret_rotation = new TurretRotation();

		//Karina's Stuff
		sys_driveTrain = new DriveTrain(); 

		joy_main = new XboxController(0);
		joy_secondary = new XboxController(1);

		cmd_turret_run = new RunTurretFlywheel(sys_turret_flywheel, sys_Indexer, joy_main, joy_secondary);

		grp_configure_turret = new SequentialCommandGroup(new ConfigureTurret(sys_turret_rotation, sys_turret_flywheel),
				new RotateTurret(sys_turret_rotation, 0));

		but_main_A = new JoystickButton(joy_main, XboxController.Button.kA.value);
		but_main_B = new JoystickButton(joy_main, XboxController.Button.kB.value);
		but_main_X = new JoystickButton(joy_main, XboxController.Button.kX.value);
		but_main_Y = new JoystickButton(joy_main, XboxController.Button.kY.value);
		// Run intake while held
		but_main_Y.whileHeld(new IntakeActive(sys_intakeSubsystem));
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
		// Toggle AntiTip
		but_secondary_Y.whenPressed(new AntiTipToggle(sys_driveTrain));
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

		//WuTang's Stuff
		but_main_start.whenPressed(new Extend(m_hanging));
		but_main_back.whenPressed(new Retract(m_hanging));
	}

	  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {

    // Create a voltage constraint to ensure we don't accelerate too fast
    final var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.kvVoltSecondsPerMeter,
            Constants.Trajectory.kaVoltSecondsSquaredPerMeter),
        Constants.Trajectory.kDriveKinematics, 10);

    // Create config for trajectory
    final TrajectoryConfig config = new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond,
        Constants.Trajectory.kMaxAccelerationMetersPerSecondSquare)

            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.Trajectory.kDriveKinematics)

            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow. All units in meters.
    final Trajectory trajectory = TrajectoryGenerator.generateTrajectory(

        // Start at the origin facing the +X direction
        new Pose2d(0, 0, new Rotation2d(0)),

        // Pass through these two interior waypoints, making an 's' curve path
        List.of(new Translation2d(1, 1), new Translation2d(2, -1)),

        // End 3 meters straight ahead of where we started, facing forward
        new Pose2d(3, 0, new Rotation2d(0)),

        // Pass config
        config);

    final RamseteCommand ramseteCommand = new RamseteCommand( 

        trajectory, sys_driveTrain::getPose, new RamseteController(Constants.Trajectory.kRamseteB, Constants.Trajectory.kRamseteZeta),

        new SimpleMotorFeedforward(Constants.Trajectory.ksVolts, Constants.Trajectory.kvVoltSecondsPerMeter,

            Constants.Trajectory.kaVoltSecondsSquaredPerMeter),

        Constants.Trajectory.kDriveKinematics, sys_driveTrain::getWheelSpeeds, new PIDController(Constants.Trajectory.kPDriveVel, 0, 0),

        new PIDController(Constants.Trajectory.kPDriveVel, 0, 0),

        // RamseteCommand passes volts to the callback
        sys_driveTrain::tankDriveVolts, sys_driveTrain);

    // Run path following command, then stop at the end.
    return ramseteCommand.andThen(() -> sys_driveTrain.tankDriveVolts(0, 0));
  }
}
