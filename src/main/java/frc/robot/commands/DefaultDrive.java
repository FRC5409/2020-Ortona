// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants.kDriveTrain;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Pigeon;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class DefaultDrive extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveTrain drive;
  private final Pigeon pigeon;
  private final XboxController joystick;

  /**
   * Creates a new DefaultDrive
   *.
   *
   * @param subsystem The subsystem used by this command.
   * @param joystick The input device used by this command.
   */
  public DefaultDrive(DriveTrain _drive, Pigeon _pigeon, XboxController _joystick) {
    drive = _drive;
    pigeon = _pigeon;
    joystick = _joystick;

    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
      // Case to determine what control scheme to utilize 
      switch(drive.getDriveMode()){
          case kDriveTrain.AADIL_DRIVE: //1
            aadilDriveExecute();
            break;
          case kDriveTrain.TANK_DRIVE: //2
            tankDriveExecute();
            break;
          default:
            aadilDriveExecute();
      }
      drive.displayDriveMode();
  }

  /**
   * This method runs the robot with the aadilDrive control scheme
   */
  private void aadilDriveExecute(){
    double leftTrigger = joystick.getLeftTriggerAxis();
    double rightTrigger = joystick.getRightTriggerAxis();
    double lxAxis = joystick.getLeftX() * -1;

    double pitch = 90/pigeon.Pitch();
    double roll  = 90/pigeon.Roll(); // might need to transform roll depending on where 0 is

    double pitchCompensation = drive.getAntiTip() ? pitch*kDriveTrain.pitchCompensation : 0;
    double rollCompensation = drive.getAntiTip() && (leftTrigger != 0) && (rightTrigger != 0)? roll * kDriveTrain.rollCompensation : 0;

    drive.aadilDrive(Math.max(rightTrigger - pitchCompensation - rollCompensation, 0), 
                     Math.max(leftTrigger + pitchCompensation + rollCompensation, 0), 
                     lxAxis);
  }

  /**
   * This method runs the robot with the tankDrive control scheme
   */
  private void tankDriveExecute(){

      double lyAxis = joystick.getLeftY() * -1; 
      double ryAxis = joystick.getRightY() * -1;

      drive.tankDrive(lyAxis, ryAxis);
  }
  
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
