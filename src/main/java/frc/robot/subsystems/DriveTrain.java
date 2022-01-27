package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants.kDriveTrain;

public class DriveTrain extends SubsystemBase{
    
    private final CANSparkMax mot_leftFrontDrive;
    private final CANSparkMax mot_leftRearDrive;
    private final CANSparkMax mot_rightFrontDrive;
    private final CANSparkMax mot_rightRearDrive;

    private final RelativeEncoder enc_leftFront;
    private final RelativeEncoder enc_rightFront;
    private final RelativeEncoder enc_leftBack;
    private final RelativeEncoder enc_rightBack;

    private final SparkMaxPIDController pid_leftFront;
    private final SparkMaxPIDController pid_rightFront;
    private final SparkMaxPIDController pid_leftBack;
    private final SparkMaxPIDController pid_rightBack;

    private int driveMode;

    private final DifferentialDrive m_drive;

    //private final DoubleSolenoid dsl_gear;
    
    private boolean applyAntiTip;


    public DriveTrain(){
        mot_leftFrontDrive = new CANSparkMax(kDriveTrain.CANLeftDriveFront, MotorType.kBrushless);
        mot_leftFrontDrive.restoreFactoryDefaults();
        mot_leftFrontDrive.setSmartCurrentLimit(60);
        mot_leftFrontDrive.setInverted(true);

        mot_leftFrontDrive.burnFlash();

        mot_leftRearDrive = new CANSparkMax(kDriveTrain.CANLeftDriveBack, MotorType.kBrushless);
        mot_leftRearDrive.restoreFactoryDefaults();
        mot_leftRearDrive.setSmartCurrentLimit(60);
        mot_leftRearDrive.follow(mot_leftFrontDrive);
        
        mot_leftRearDrive.setInverted(true);
        mot_leftRearDrive.burnFlash();

        mot_rightFrontDrive = new CANSparkMax(kDriveTrain.CANRightDriveFront, MotorType.kBrushless);
        mot_rightFrontDrive.restoreFactoryDefaults();
        mot_rightFrontDrive.setSmartCurrentLimit(60);
        //mot_rightFrontDrive.setInverted(true);
        
        mot_rightFrontDrive.burnFlash();

        mot_rightRearDrive = new CANSparkMax(kDriveTrain.CANRightDriveBack, MotorType.kBrushless);
        mot_rightRearDrive.restoreFactoryDefaults();
        mot_rightRearDrive.setSmartCurrentLimit(60);

        mot_rightRearDrive.follow(mot_rightFrontDrive);
        mot_rightRearDrive.burnFlash();

        m_drive = new DifferentialDrive(mot_leftFrontDrive, mot_rightFrontDrive);

        enc_leftFront = mot_leftFrontDrive.getEncoder();
        enc_rightFront = mot_rightFrontDrive.getEncoder();
        enc_leftBack = mot_leftRearDrive.getEncoder();
        enc_rightBack = mot_rightRearDrive.getEncoder();


        //dsl_gear = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, kDriveTrain.ForwardChannel, kDriveTrain.ReverseChannel);

        pid_leftFront = mot_leftFrontDrive.getPIDController();
        pid_leftBack = mot_leftRearDrive.getPIDController();
        pid_rightFront = mot_rightFrontDrive.getPIDController();
        pid_rightBack = mot_rightRearDrive.getPIDController();

        driveMode = kDriveTrain.InitialDriveMode;
    }

    /**
     * This method is called once per scheduler run and is used to update smart dashboard data.
     */
    public void periodic() {

    }

    @Override
    public void simulationPeriodic() {
        displayEncoder();
        displayDriveMode();
    }

    // ------------------------- Drive Modes ------------------------- //

    /**
     * @param acceleration the robot's forward speed 
     * 
     * @param deceleration the robot's backward speed
     * 
     * @param turn         the robot's angular speed about the z axis
     * 
     */
    public void aadilDrive(final double acceleration, final double deceleration, final double turn){
        double accelerate = acceleration - deceleration;

        m_drive.arcadeDrive(accelerate, turn, true);
    }
    
    /**
     * @param leftSpeed the robot's left side speed along the X axis [-1.0, 1.0]. Forward is positive.
     * 
     * @param rightSpeed the robot's right side speed along the X axis [-1.0, 1.0]. Forward is positive.
     * 
     */
    public void tankDrive(double leftSpeed, double rightSpeed){
        m_drive.tankDrive(leftSpeed, rightSpeed);
    }

    /**
     * @return the current drive mode as an int
     * 
     */
    public int getDriveMode(){
        return driveMode;
    }

    /**
     * @return the current drive mode as a String
     * 
     */
    private String getDriveModeString(){
        switch(driveMode){
            case kDriveTrain.AADIL_DRIVE:
                return "AADIL DRIVE";

            case kDriveTrain.TANK_DRIVE:
                return "TANK DRIVE";

            default:
                return "NONE";
        } 
    }

    /**
     * Puts the current drive mode into SmartDashboard 
     * 
     */
    public void displayDriveMode(){
        SmartDashboard.putString("Drive Mode", getDriveModeString());
    }

    /**
     * directly set the drive mode to the specified int
     * 
     */
    public void setDriveMode(int newDriveMode){
        driveMode = newDriveMode;
    }

    /**
     * cycles to the next drive mode
     * 
     * Order:
     *      AADIL_DRIVE
     *      TANK_DRIVE
     *      REPEAT
     */
    public void cycleDriveMode(){
        System.out.println("Cycling drive mode");
        switch(driveMode){
            case kDriveTrain.AADIL_DRIVE:
                driveMode = kDriveTrain.TANK_DRIVE;
                break; 

            case kDriveTrain.TANK_DRIVE:
                driveMode = kDriveTrain.AADIL_DRIVE;
                break; 
        } 
    }
    /**
     * @param enable if true sets all motors to brake, if false sets all motors to coast.
     * 
     */
    public void setBrakeMode(boolean enable){
        if(enable){
            mot_leftFrontDrive.setIdleMode(IdleMode.kBrake);
            mot_leftRearDrive.setIdleMode(IdleMode.kBrake);
            mot_rightFrontDrive.setIdleMode(IdleMode.kBrake);
            mot_rightRearDrive.setIdleMode(IdleMode.kBrake);
        }
        else{
            mot_leftFrontDrive.setIdleMode(IdleMode.kCoast);
            mot_leftRearDrive.setIdleMode(IdleMode.kCoast);
            mot_rightFrontDrive.setIdleMode(IdleMode.kCoast);
            mot_rightRearDrive.setIdleMode(IdleMode.kCoast);
        }
        
    }

    // ---------------------------- Anti Tip ---------------------------- //

    /**
     * @return true if the anti tip should be applied 
     * 
     */
    public boolean getAntiTip(){
        return applyAntiTip;
    }

    /**
     * @param boolean the new state of applyAntiTip
     * 
     * Sets applyAntiTip
     * 
     */
    public void setAntiTip(boolean _applyAntiTip){
        applyAntiTip = !_applyAntiTip;
    }

    /**
     * toggles the anti-tip
     * 
     */
    public void toggleAntiTip(){
        applyAntiTip = !applyAntiTip;
    }

    // ------------------------- Drive Modes ------------------------- //

    public void setPIDGains(double P, double I, double D){
        pid_leftFront.setP(P);
        pid_leftFront.setI(I);
        pid_leftFront.setD(D);

        pid_leftBack.setP(P);
        pid_leftBack.setI(I);
        pid_leftBack.setD(D);

        pid_rightFront.setP(P);
        pid_rightFront.setI(I);
        pid_rightFront.setD(D);
        
        pid_rightBack.setP(P);
        pid_rightBack.setI(I);
        pid_rightBack.setD(D);
    }

    public void setControlMode(double setpoint, ControlType mode){
        pid_leftFront.setReference(setpoint, mode);
        pid_leftBack.setReference(setpoint, mode);
        pid_rightFront.setReference(setpoint, mode);
        pid_rightBack.setReference(setpoint, mode);
    }

    public void setControlMode(double setpointLeft, double setpointRight, ControlType mode){
        pid_leftFront.setReference(setpointLeft, mode);
        pid_leftBack.setReference(setpointLeft, mode);
        pid_rightFront.setReference(setpointRight, mode);
        pid_rightBack.setReference(setpointRight, mode);
    }

    // ---------------------------- Encoders ---------------------------- //

    /**
     * Puts the positions and velocities of the left and right encoders into SmartDashboard 
     * 
     */
    public void displayEncoder(){
        SmartDashboard.putNumber("Left Position", getEncoderPositionLeft());
        SmartDashboard.putNumber("Left Velocity", getEncoderVelocityLeft());

        SmartDashboard.putNumber("Right Position", getEncoderPositionRight());
        SmartDashboard.putNumber("Right Velocity", getEncoderVelocityRight());
    }

    /**
     * @return the average position of all four encoders 
     * 
     */
    public double getEncoderPosition(){
        return (getEncoderPositionLeft() + getEncoderPositionRight() )/ 2;
    }

    /**
     * @return the average position of the left encoders 
     * 
     */
    public double getEncoderPositionLeft(){
        return enc_leftFront.getPosition() + enc_leftBack.getPosition() / 2;
    }

    /**
     * @return the average position of the right encoders 
     * 
     */
    public double getEncoderPositionRight(){
        return enc_rightFront.getPosition() + enc_rightBack.getPosition() / 2;
    }

    /**
     * @return the average velocity all for encoders
     * 
     */
    public double getEncoderVelocity(){
        return (getEncoderVelocityLeft() + getEncoderVelocityRight() )/ 2;
    }

    /**
     * @return the average velocity of the left encoders 
     * 
     */
    public double getEncoderVelocityLeft(){
        return enc_leftFront.getVelocity() + enc_leftBack.getVelocity() / 2;
    }

    /**
     * @return the average velocity of the right encoders 
     * 
     */ 
    public double getEncoderVelocityRight(){
        return enc_rightFront.getVelocity() + enc_rightBack.getVelocity() / 2;
    }
    /**
     * Sets all encoders to 0
     * 
     * call this function when reading positions relative to the robot's current position.
     * 
     */ 
    public void zeroEncoders() {
        enc_leftBack.setPosition(0);
        enc_leftFront.setPosition(0);
        enc_rightBack.setPosition(0);
        enc_rightFront.setPosition(0);
    }

    // ---------------------------- Solenoids ---------------------------- //

    /**
     * shifts the gear shift to fast 
     */ 
    public void fastShift(){
        SmartDashboard.putString("Solenoid", "Fast");
        //dsl_gear.set(DoubleSolenoid.Value.kForward);
    }

    /**
     * shifts the gear shift to slow 
     */ 
    public void slowShift(){
        SmartDashboard.putString("Solenoid", "Slow");
        //dsl_gear.set(DoubleSolenoid.Value.kReverse);
    }


}
