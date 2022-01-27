package org.frc.team5409.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.*;

import org.frc.team5409.robot.subsystems.shooter.*;
import org.frc.team5409.robot.util.AutoCommand;
import org.frc.team5409.robot.commands.shooter.*;
import org.frc.team5409.robot.subsystems.*;
import org.frc.team5409.robot.commands.*;

public class ComplexAuto extends SequentialCommandGroup implements AutoCommand {
    private boolean m_shooting, m_driving, m_intaking, m_finished;

    /**
     * Creates a new shootAuto.
     */
    public ComplexAuto(ShooterFlywheel sys_flywheel, ShooterTurret sys_rotation, Limelight sys_limelight, Indexer sys_indexer, DriveTrain sys_driveTrain, Intake sys_intake) {
        addCommands(
            internal_decorate(new OperateShooter(sys_flywheel, sys_rotation, sys_limelight, sys_indexer), AutonomousState.kShooting).withTimeout(3.5),

            new ParallelCommandGroup(
                internal_decorate(new DriveStraightAuto(sys_driveTrain, 0.75, 0.75), AutonomousState.kDriving),
                internal_decorate(new RotateTurret(sys_rotation, 0), AutonomousState.kShooting),
                internal_decorate(new IntakeIndexActiveAuto(sys_indexer, sys_intake), AutonomousState.kIntaking)
            ).withTimeout(3),

            internal_decorate(new DriveStraightAuto(sys_driveTrain, -0.8, -0.8), AutonomousState.kDriving).withTimeout(2.5),
            internal_decorate(new DriveStraightAuto(sys_driveTrain, 0.1, 0.1), AutonomousState.kDriving).withTimeout(1),

            internal_decorate(new OperateShooter(sys_flywheel, sys_rotation, sys_limelight, sys_indexer), AutonomousState.kShooting).withTimeout(6)
        );
        
        m_shooting = false;
        m_driving = false;
        m_intaking = false;
        m_finished = false;

        /*var parent = Shuffleboard.getTab("Robot Information").getLayout("Autonomous Information", BuiltInLayouts.kList);
            parent.addBoolean("Autonomous Finished", () -> { return m_finished; });
            var child = parent.getLayout("Autonomous State", BuiltInLayouts.kGrid);
                child.addBoolean("Shooting State", () -> { return m_shooting; });
                child.addBoolean("Driving State", () -> { return m_driving; });
                child.addBoolean("Intaking State", () -> { return m_intaking; });*/
    }

    private Command internal_decorate(Command cmd, AutonomousState state) {
        return cmd.beforeStarting(() -> { internal_setState(state, true); })
                  .andThen(() -> { internal_setState(state, false); });
    }

    private void internal_setState(AutonomousState type, boolean state) {
        switch (type) {
            case kShooting: m_shooting = state; break;
            case kDriving: m_driving = state; break;
            case kIntaking: m_intaking = state; break;
            case kFinished: m_finished = state; break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        internal_setState(AutonomousState.kFinished, true);
        super.end(interrupted);
    }

    @Override
    public boolean getState(AutonomousState state) {
        switch (state) {
            case kDriving: return m_driving;
            case kShooting: return m_shooting;
            case kIntaking: return m_intaking;
            case kFinished: return m_finished;
        }
        return false;
    }
}
