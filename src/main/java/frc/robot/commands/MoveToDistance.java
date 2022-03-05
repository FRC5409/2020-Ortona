package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveTrain;

public class MoveToDistance extends CommandBase {

    private DriveTrain drive;
    private double setpoint;
    private boolean useSmartDashboard;

    public MoveToDistance(DriveTrain _drive){
        drive = _drive;
        setpoint = 0;
        useSmartDashboard = true;
    }

    public MoveToDistance(DriveTrain _drive, double _setpoint){
        drive = _drive;
        setpoint = _setpoint;
        useSmartDashboard = false;
    }

    @Override
    public void initialize(){
        drive.zeroEncoders();
        if(useSmartDashboard){
            if(SmartDashboard.containsKey("target distance")){
                drive.setControlMode(SmartDashboard.getNumber("target distance", 0), ControlType.kPosition);
            }
        }
        else{
            drive.setControlMode(setpoint, ControlType.kPosition);
        }
    }

    @Override
    public void end(boolean interupted){
        drive.setControlMode(0, ControlType.kDutyCycle);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return drive.getEncoderPosition() == setpoint;
    }
}