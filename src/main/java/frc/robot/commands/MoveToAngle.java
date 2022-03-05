package frc.robot.commands;

import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

public class MoveToAngle extends CommandBase {

    private DriveTrain drive;
    private double setpoint;
    private boolean useSmartDashboard;

    public MoveToAngle(DriveTrain _drive){
        drive = _drive;
        setpoint = 0; // calculate distance                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             useSmartDashboard = true;
    }

    public MoveToAngle(DriveTrain _drive, double _setpoint){
        drive = _drive;
        setpoint =  CalculateDistance(_setpoint); // calculate distance
        useSmartDashboard = true;
    }

    private static double CalculateDistance(double angle){
        return Math.toRadians(angle) * Math.PI * Constants.kDriveTrain.wheelSeparation;
    }

    @Override
    public void initialize(){
        if(useSmartDashboard){
            if(SmartDashboard.containsKey("target distance")){
                drive.setControlMode(CalculateDistance(SmartDashboard.getNumber("target distance", 0)), 
                                    -CalculateDistance(SmartDashboard.getNumber("target distance", 0)), 
                                     ControlType.kPosition);
            }
        }
        else{
            drive.setControlMode(setpoint, -setpoint, ControlType.kPosition);
        }
    }

    @Override
    public void end(boolean interrupt){
        drive.setControlMode(0, ControlType.kDutyCycle);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return drive.getEncoderPositionRight() == setpoint;
    }
}