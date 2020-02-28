/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.subsystems.DriveTrain;

public class DriveCommand extends CommandBase {
  private final DriveTrain sys_driveSubsystem;
  public static boolean autoBalanceXMode = false;
  public static boolean autoBalanceYMode = false;
  public static double pitchAngleDegrees;
  public static double rollAngleDegrees;
  // Temporary threshold values, need to test
  static final double kOffBalanceAngleThresholdDegrees = 10;
  static final double kOonBalanceAngleThresholdDegrees = 5;
  static final double highGearShiftThreshold = 100;
  static final double lowGearShiftThreshold = 20; 
  public double rightTrigger;
  public double leftTrigger;
  public double lxAxis;
  public double lyAxis;
  public double leftEncoderRate;
  public double rightEncoderRate;
  public double averageEncoderRate;
  XboxController m_joystick;

  /**
   * Creates a new DriveCommand.
   */
  public DriveCommand(DriveTrain subsystem, XboxController joystick) {
    sys_driveSubsystem = subsystem;
    m_joystick = joystick;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(sys_driveSubsystem);
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
    pitchAngleDegrees = sys_driveSubsystem.getPitchAngle();
    rollAngleDegrees = sys_driveSubsystem.getRollAngle();
    leftEncoderRate = sys_driveSubsystem.getLeftEncoderRate();
    rightEncoderRate = sys_driveSubsystem.getRightEncoderRate();
    averageEncoderRate = (leftEncoderRate + rightEncoderRate)/2;


    // if(averageEncoderRate <= lowGearShiftThreshold){
    //   sys_driveSubsystem.slowShift();
    // }
    // if(averageEncoderRate >= highGearShiftThreshold){
    //   sys_driveSubsystem.fastShift();
    // }

    // Detect the toggle value
    if (sys_driveSubsystem.getAntiTip()) {
      // Threshold detection to enable autoBalanceXMode
      if (!autoBalanceXMode && (Math.abs(pitchAngleDegrees) >= Math.abs(kOffBalanceAngleThresholdDegrees))) {
        autoBalanceXMode = true;
      }

      if (!autoBalanceYMode && (Math.abs(pitchAngleDegrees) >= Math.abs(kOffBalanceAngleThresholdDegrees))) {
        autoBalanceYMode = true;
      }

      if (autoBalanceYMode && (Math.abs(pitchAngleDegrees) <= Math.abs(kOonBalanceAngleThresholdDegrees))) {
        autoBalanceYMode = false;
      }
  
      if (autoBalanceXMode && (Math.abs(pitchAngleDegrees) <= Math.abs(kOonBalanceAngleThresholdDegrees))) {
        autoBalanceXMode = false;
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
        sys_driveSubsystem.arcadeDrive(lxAxis, lyAxis);
      } catch (RuntimeException ex) {
        String err_string = "Drive system error:  " + ex.getMessage();
        DriverStation.reportError(err_string, true);
      }
    } else { //If AntiTip is disabled, default to manual drive
        sys_driveSubsystem.manualDrive(leftTrigger, rightTrigger, lxAxis);
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