/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.commands.Trajectories;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.frc.team5409.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class FirstPath extends CommandBase {
  DriveTrain sys_driveTrain;
  TrajectoryConfig config;
  String trajectoryJSON;
  Trajectory trajectory;

  /**
   * Creates a new anotherPath.
   */
  public FirstPath() {
    config = new TrajectoryConfig(Units.feetToMeters(2), Units.feetToMeters(2));
    config.setKinematics(sys_driveTrain.getKinematics());
    trajectoryJSON = "paths/firstPath.wpilib.json";
    try {
			Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
			trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
		} catch (IOException ex) {
			trajectory = TrajectoryGenerator.generateTrajectory(Arrays.asList(new Pose2d()), config);
			DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
		}
  }
  RamseteCommand ramseteCommand = new RamseteCommand(
    trajectory, 
    sys_driveTrain::getPose, 
    new RamseteController(), 
    sys_driveTrain.getFeedforward(), 
    sys_driveTrain.getKinematics(), 
    sys_driveTrain::getWheelSpeeds, 
    sys_driveTrain.getLeftPIDController(), 
    sys_driveTrain.getRightPIDController(), 
    sys_driveTrain::setOutputVoltage, 
    sys_driveTrain
  );
}
