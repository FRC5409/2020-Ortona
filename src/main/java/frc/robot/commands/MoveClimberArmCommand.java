// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Climber;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class MoveClimberArmCommand extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final XboxController m_joystick;
    private final Climber m_subsystem;

    /**
     * Creates a new ExampleCommand.
     *
     * @param subsystem The subsystem used by this command.
     */
    public MoveClimberArmCommand(Climber subsystem, XboxController joystick) {
        m_subsystem = subsystem;
        m_joystick = joystick;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        int direction;

        if (m_subsystem.getDirection() == Constants.Climber.DIRECTION_EXTEND) {
            direction = Constants.Climber.DIRECTION_RETRACT;
        } else {
            direction = Constants.Climber.DIRECTION_EXTEND;
        }

        m_subsystem.setDirection(direction);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Check if up d-pad is pressed
        boolean extending = m_joystick.getPOV(0) != -1;
        // Check if down d-pad is pressed
        boolean retracting = m_joystick.getPOV(180) != -1;

        // If up is pressed, set direction to extend
        if (extending){
            m_subsystem.setDirection(Constants.Climber.DIRECTION_EXTEND);
        } else if (retracting) {    // If down is pressed set direction to retracts
            m_subsystem.setDirection(Constants.Climber.DIRECTION_RETRACT);
        }

        // Move the arm as long as arm is either retracting or extending
        if (extending || retracting) {
            m_subsystem.moveArm();
        }

        // m_subsystem.extendArm(m_rate);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (m_subsystem.getLength() == Constants.Climber.EXTENSION_LENGTH
                && m_subsystem.getDirection() == Constants.Climber.DIRECTION_EXTEND)
                || (m_subsystem.getLength() == Constants.Climber.RETRACTION_LENGTH
                        && m_subsystem.getDirection() == Constants.Climber.DIRECTION_RETRACT);
    }
}
