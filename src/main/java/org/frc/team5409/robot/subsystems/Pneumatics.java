/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pneumatics extends SubsystemBase {
  /**
   * Creates a new Pneumatics.
   */
  Compressor compressor1;
  public Pneumatics() {
    compressor1 = new Compressor(0);
    compressAir();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void compressAir(){
    //compressor1.setClosedLoopControl(true);
}

  public void releaseAir(){
    compressor1.setClosedLoopControl(false);
}

}
