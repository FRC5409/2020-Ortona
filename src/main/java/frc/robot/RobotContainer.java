// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


// Subsystems
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Pigeon;

// Commands
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.DefaultDrive;
import frc.robot.commands.SimpleDriveAuto;
import frc.robot.commands.FastGear;
import frc.robot.commands.SlowGear;
import frc.robot.commands.SetAntiTip;
import frc.robot.commands.ToggleAntiTip;
import frc.robot.commands.MoveToDistance;
import frc.robot.commands.MoveToAngle;

// Misc
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.IntakeIndexerCommands;
import frc.robot.commands.MoveToAngle;
import frc.robot.commands.MoveToDistance;
import frc.robot.subsystems.IntakeIndexer;
import edu.wpi.first.wpilibj2.command.Command;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  //private final IntakeIndexer m_intakeindexer = new IntakeIndexer();

  //private final IntakeIndexerCommands m_autoCommand = new IntakeIndexerCommands(m_intakeindexer);

  // Define main joystick
  private final XboxController joystick_main; // = new XboxController(0);
  private final JoystickButton but_main_A, but_main_B, but_main_X, but_main_Y, but_main_LBumper, but_main_RBumper,
      but_main_LAnalog, but_main_RAnalog, but_main_Back, but_main_Start;
  
      
  // Subsystems defined
  private final DriveTrain DriveTrain;
  //private final Pigeon Pigeon;

  // Commands defined
  //private final ExampleCommand m_autoCommand;
  private final DefaultDrive defaultDrive;


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Init controller
    joystick_main = new XboxController(0);

    // Init button binds
    but_main_A = new JoystickButton(joystick_main, XboxController.Button.kA.value);
    but_main_B = new JoystickButton(joystick_main, XboxController.Button.kB.value);
    but_main_X = new JoystickButton(joystick_main, XboxController.Button.kX.value);
    but_main_Y = new JoystickButton(joystick_main, XboxController.Button.kY.value);
    but_main_LBumper = new JoystickButton(joystick_main, XboxController.Button.kLeftBumper.value);
    but_main_RBumper = new JoystickButton(joystick_main, XboxController.Button.kRightBumper.value);
    but_main_LAnalog = new JoystickButton(joystick_main, XboxController.Button.kLeftStick.value);
    but_main_RAnalog = new JoystickButton(joystick_main, XboxController.Button.kRightStick.value);
    but_main_Back = new JoystickButton(joystick_main, XboxController.Button.kBack.value);
    but_main_Start = new JoystickButton(joystick_main, XboxController.Button.kStart.value);

     // Initialize sub systems
     DriveTrain = new DriveTrain();
     //Pigeon = new Pigeon();

     // Init commands
     defaultDrive = new DefaultDrive(DriveTrain, joystick_main);

 
    // Configure the button bindings
    configureButtonBindings();

    // temp
    SmartDashboard.putNumber("target distance", 0);
    SmartDashboard.putNumber("target angle", 0);

    // Sets default command to be DefaultDrive
    DriveTrain.setDefaultCommand(defaultDrive);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    // Bind start to go to the next drive mode
    but_main_Back.whenPressed(() -> DriveTrain.cycleDriveMode());
    but_main_Start.whenPressed( new ToggleAntiTip(DriveTrain));

    // Bind right bumper to 
    but_main_RBumper.whenPressed(new FastGear(DriveTrain));
    but_main_RBumper.whenReleased( new SlowGear(DriveTrain));

    //but_main_A.toggleWhenPressed( new MoveToDistance(DriveTrain, 10));
    //but_main_B.toggleWhenPressed( new MoveToAngle(DriveTrain, 90));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
     
    return new SimpleDriveAuto(DriveTrain);
  }
}
