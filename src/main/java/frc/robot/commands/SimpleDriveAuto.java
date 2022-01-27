package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveTrain;

public class SimpleDriveAuto extends SequentialCommandGroup {

    public SimpleDriveAuto(DriveTrain sys_drive) {
        addCommands(
            new DriveStraight(sys_drive, -0.2f).withTimeout(1)
        );
    }
}