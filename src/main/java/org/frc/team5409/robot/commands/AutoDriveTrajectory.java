package org.frc.team5409.robot.commands;


import java.util.List;
import java.util.function.BiConsumer;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.subsystems.DriveTrain;

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
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

public final class AutoDriveTrajectory extends CommandBase {
    private final DriveTrain m_drivetrain;

    private final RamseteCommand m_ramsete;

    public AutoDriveTrajectory(DriveTrain sys_drivetrain) {
        m_drivetrain = sys_drivetrain;

        final var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
            new SimpleMotorFeedforward(
                Constants.Trajectory.ksVolts, 
                Constants.Trajectory.kvVoltSecondsPerMeter,
                Constants.Trajectory.kaVoltSecondsSquaredPerMeter
            ),
            Constants.Trajectory.kDriveKinematics,
            10
        );

        final TrajectoryConfig config = new TrajectoryConfig(Constants.Trajectory.kMaxSpeedMetersPerSecond,
            Constants.Trajectory.kMaxAccelerationMetersPerSecondSquare)
                .setKinematics(Constants.Trajectory.kDriveKinematics)
                .addConstraint(autoVoltageConstraint);

        final Trajectory trajectory = TrajectoryGenerator.generateTrajectory(
            new Pose2d(0, 0, new Rotation2d(0)),
            List.of(new Translation2d(1, 1), new Translation2d(2, -1)),
            new Pose2d(3, 0, new Rotation2d(0)),
            config
        );

        m_ramsete = new RamseteCommand( 
            trajectory,
            m_drivetrain::getPose,
            new RamseteController(
                Constants.Trajectory.kRamseteB,
                Constants.Trajectory.kRamseteZeta
            ),
            new SimpleMotorFeedforward(
                Constants.Trajectory.ksVolts,
                Constants.Trajectory.kvVoltSecondsPerMeter,
                Constants.Trajectory.kaVoltSecondsSquaredPerMeter
            ),
            Constants.Trajectory.kDriveKinematics,
            m_drivetrain::getWheelSpeeds,
            new PIDController(
                Constants.Trajectory.kPDriveVel,
                0,
                0
            ),
            new PIDController(
                Constants.Trajectory.kPDriveVel,
                0,
                0
            ),
            m_drivetrain::tankDriveVolts,
            m_drivetrain
        );
    }

    @Override
    public void initialize() {
        m_ramsete.initialize();
    }

    @Override
    public void execute() {
        m_ramsete.execute();
    }

    @Override
    public void end(boolean interrupted) {
        m_ramsete.end(interrupted);

        m_drivetrain.tankDriveVolts(0, 0);
    }

    @Override
    public boolean isFinished() {
        return m_ramsete.isFinished();
    }
}
