package org.frc.team5409.robot;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
  private Main() {
  }

	/**
	 * Main initialization function. Do not perform any initialization here.
	 *
	 * <p>If you change your main robot class, change the parameter type.
	 */
  	public static void main(String... args) {
		System.out.println("DOUGLAS FOR THE WIN! - Keith");
		
    	RobotBase.startRobot(Robot::new);
  	}
}
