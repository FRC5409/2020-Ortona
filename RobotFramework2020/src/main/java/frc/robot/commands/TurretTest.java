// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.subsystems.TimeofFlight;

// public final class TurretTest extends CommandBase {

//     boolean TOF_Enter;
// boolean TOF_Exit;
// boolean TOF_Ball1;
// boolean indexerRun;
// int ballCounter;
// boolean ballAtPosition1;


// public TurretTest(TimeofFlight Subsystem) {
//     m_timeOfFlight = Subsystem;
//     addRequirements(Subsystem);
//     TOF_Enter = m_timeOfFlight.ballDetectionEnter();
// 		TOF_Ball1 = m_timeOfFlight.ballDetectionBall1();
//         TOF_Exit = m_timeOfFlight.ballDetectionExit();
    
// }
    
//     @Override
//     public void initialize() {

        
        
//     }

//     @Override
//     public void execute() {
//         if (TOF_Enter == false) {
// 			if (TOF_Ball1 == true) {
// 				indexerRun = true;
// 			}

// 			if (TOF_Exit == true) {
// 				ballAtPosition1 = true;
// 			}

// 			if (TOF_Ball1 == false && ballAtPosition1 == true) {
// 				indexerRun = false;
// 			} else if (TOF_Ball1 == true && ballAtPosition1 == true) {
// 				ballAtPosition1 = false;
// 				indexerRun = true;
// 			}

// 			if (TOF_Enter == true) {
// 				indexerRun = false;
// 			}

// 			if (indexerRun == true) {
// 				m_timeOfFlight.moveIndexerMotor(0.5);
// 			} else {
// 				m_timeOfFlight.moveIndexerMotor(0);
// 			}

// 		}
//     }

//     @Override
//     public void end(boolean interrupted) {
//         // TODO Auto-generated method stub
//         super.end(interrupted);
//     }

//     @Override
//     public boolean isFinished() {
//         // TODO Auto-generated method stub
//         return super.isFinished();
//     }
// }