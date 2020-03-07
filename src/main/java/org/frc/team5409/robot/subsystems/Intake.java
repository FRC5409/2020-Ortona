/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc.team5409.robot.subsystems;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.frc.team5409.robot.Constants;

public class Intake extends SubsystemBase {
	private CANSparkMax mot_intake_sparkMax_C12;
	private final CANEncoder intakeEncoder;
	private DoubleSolenoid dsl_rightIntakeSolenoid;	
	private DoubleSolenoid dsl_leftIntakeSolenoid;

	/**
	 * Creates a new Intake.
	 */
	public Intake() {
		mot_intake_sparkMax_C12 = new CANSparkMax(Constants.Intake.kIntakeMotor, MotorType.kBrushless);	
			mot_intake_sparkMax_C12.setSmartCurrentLimit(20); 	
			mot_intake_sparkMax_C12.setIdleMode(IdleMode.kBrake);	
		mot_intake_sparkMax_C12.burnFlash();  	

		dsl_rightIntakeSolenoid = new DoubleSolenoid(Constants.Intake.kRightIntakeSolenoid1, Constants.Intake.kRightIntakeSolenoid2);	
		dsl_leftIntakeSolenoid = new DoubleSolenoid(Constants.Intake.kLeftIntakeSolenoid1, Constants.Intake.kLeftIntakeSolenoid2);

		intakeEncoder = mot_intake_sparkMax_C12.getEncoder();

		/*Shuffleboard.getTab("Robot Information").getLayout("Intake Information", BuiltInLayouts.kList)
				    .addBoolean("Intake Jammed", this::isIntakeJammed);*/
	}

	/**	
	 * Method to turn intake on	
	 */	
	public void intakeOn(double output) {	

		mot_intake_sparkMax_C12.set(output);	

	}	


	/**	
	 * method to put intake down	
	 */	
	public void solenoidsDown(){	

		dsl_rightIntakeSolenoid.set(DoubleSolenoid.Value.kForward);	
		dsl_leftIntakeSolenoid.set(DoubleSolenoid.Value.kForward);	

	}	

	public boolean isExtended(){	

		return (dsl_leftIntakeSolenoid.get() == DoubleSolenoid.Value.kForward && 	
		dsl_rightIntakeSolenoid.get() == DoubleSolenoid.Value.kForward); 	

	}	


	/**	
	 * method to raise intake up	
	 */	
	public void solenoidsUp(){	

		dsl_rightIntakeSolenoid.set(DoubleSolenoid.Value.kReverse);	
		dsl_leftIntakeSolenoid.set(DoubleSolenoid.Value.kReverse);	

	}	
	/**	
	 * Method to reverse intake, in case of jamming	
	 */	
	public void reverse(double output) {	

		// dsl_rightIntakeSolenoid.set(DoubleSolenoid.Value.kForward);	
		// dsl_leftIntakeSolenoid.set(DoubleSolenoid.Value.kForward);	

		mot_intake_sparkMax_C12.set(output);	

	}

	public boolean isIntakeJammed() {
		return !(intakeEncoder.getVelocity() >= Constants.Intake.velocityMaxIntakeJam);
	}
}
