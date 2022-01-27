package frc.robot.commands;

import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.CommandBase;


public class DriveStraight extends CommandBase {
    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private float driveSpeed;
    private DriveTrain sys_drive;
  
    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public DriveStraight(DriveTrain subsystem, float speed) {
      sys_drive = subsystem;
      driveSpeed = speed;
      // Use addRequirements() here to declare subsystem dependencies.
      addRequirements(sys_drive);
    }
  
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}
  
    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        sys_drive.tankDrive(driveSpeed, driveSpeed);
    }
  
    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        sys_drive.tankDrive(0,0);
    }
  
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }
  }