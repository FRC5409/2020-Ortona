// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Climber extends SubsystemBase {

  private TalonFX mot_armDriver;
  
  private boolean locked;

  /**
   * Constructor for the climber.
   */
  public Climber() {

    mot_armDriver = new TalonFX(Constants.Climber.mot_port);
    locked = false;

    // Gives absolute motor positions of 0 - 360 degrees, all positive values.
    mot_armDriver.configIntegratedSensorAbsoluteRange(AbsoluteSensorRange.Unsigned_0_to_360);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void periodic() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void simulationPeriodic() {
  }

  /**
   * Method for retracting or extending the climber arm.
   */
  public void moveArm() {

    // TODO currently takes in a fixed rate.
    if(!locked){
      //currently moves the motor at a rate of 180 degrees per 100ms.
      mot_armDriver.set(TalonFXControlMode.Velocity, Constants.Climber.ARM_SPEED);
    }
  }

  /**
   * Method for locking the arm.
   */
  public void lockArm() {

    locked = true;
  }

  /**
   * Method for unlocking the arm.
   */
  public void unlockArm() {
    locked = false;
  }

  /**
   * This method can be called to toglle the locked value.
   */
  public void toggleLock() {
    locked = !locked;
  }

  /**
   * Method for getting the length of the arm extended. This is a calculated
   * value.
   * 
   * @return The length at which the arm is currently extended.
   */
  public double getLength() {
    return 0;
  }

  /**
   * This method will set the inversion of the motor
   * 
   * @param direction Direction of travel
   */
  public void setDirection(int direction) {
    // Checks to see if the request is redundant
    if (direction != getDirection()) {
      // Set the direction to extend
      if (direction == Constants.Climber.DIRECTION_EXTEND) {
        mot_armDriver.setInverted(false);
        // Set the direction to retract
      } else if (direction == Constants.Climber.DIRECTION_RETRACT) {
        mot_armDriver.setInverted(true);
      }
    }
  }

  /**
   * This method will return the direction of travel for the arm
   * 
   * @return Integer value corresponding to the direction of the motor.
   */
  public int getDirection() {
    if (!mot_armDriver.getInverted()) {
      return Constants.Climber.DIRECTION_EXTEND;
    } else {
      return Constants.Climber.DIRECTION_RETRACT;
    }
  }

}
