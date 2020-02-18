/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class DriveCommand extends CommandBase {
  private final DriveTrain m_driveSubsystem;
  public static boolean autoBalanceXMode = false;
  public static boolean autoBalanceYMode = false;
  public static double pitchAngleDegrees;
  public static double rollAngleDegrees;
  static final double kOffBalanceAngleThresholdDegrees = 10;
  static final double kOonBalanceAngleThresholdDegrees = 5;
  public double rightTrigger;
  public double leftTrigger;
  public double lxAxis;
  public double lyAxis;
  XboxController m_joystick;

  /**
   * Creates a new DriveCommand.
   */
  public DriveCommand(DriveTrain subsystem, XboxController joystick) {
    m_driveSubsystem = subsystem;
    m_joystick = joystick;
    // DriveTrain m_DriveTrainSubsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(m_driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    rightTrigger = m_joystick.getTriggerAxis(Hand.kLeft);
    leftTrigger = m_joystick.getTriggerAxis(Hand.kRight);
    lxAxis = m_joystick.getX(Hand.kLeft);
    lyAxis = rightTrigger - leftTrigger;
    pitchAngleDegrees = m_driveSubsystem.getPitchAngle();
    rollAngleDegrees = m_driveSubsystem.getRollAngle();



    // Detect the toggle value
    if (m_driveSubsystem.getAntiTip()) {
      // Threshold detection to enable autoBalanceXMode
      if (!autoBalanceXMode && (Math.abs(pitchAngleDegrees) >= Math.abs(kOffBalanceAngleThresholdDegrees))) {
        autoBalanceXMode = true;
      }

      if (!autoBalanceYMode && (Math.abs(pitchAngleDegrees) >= Math.abs(kOffBalanceAngleThresholdDegrees))) {
        autoBalanceYMode = true;
      }

      // Control drive system automatically,
      // driving in reverse direction of pitch/roll angle,
      // with a magnitude based upon the angle
      if (autoBalanceXMode) {
        double pitchAngleRadians = pitchAngleDegrees * (Math.PI / 180.0);
        lxAxis = Math.sin(pitchAngleRadians) * -1;
      }
      if (autoBalanceYMode) {
        double rollAngleRadians = rollAngleDegrees * (Math.PI / 180.0);
        lyAxis = Math.sin(rollAngleRadians) * -1;
      }
      try {
        m_driveSubsystem.arcadeDrive(lxAxis, lyAxis);
      } catch (RuntimeException ex) {
        String err_string = "Drive system error:  " + ex.getMessage();
        DriverStation.reportError(err_string, true);
      }

      // Wait for motor update time
      //WARNING!!! THIS MAY CAUSE ISSUES - Commented out//Timer.delay(0.005);
    } else { //If autotip is disabled, default to manual drive
        m_driveSubsystem.manualDrive(leftTrigger, rightTrigger, lxAxis);
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}