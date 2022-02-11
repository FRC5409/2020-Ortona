package org.frc.team5409.robot.commands.trainer;

import java.util.concurrent.Future;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.training.protocol.NetworkClient;
import frc.robot.training.protocol.NetworkStatus;
import frc.robot.training.protocol.NetworkResponse;
import frc.robot.training.protocol.NetworkRequest;
import frc.robot.training.protocol.generic.KeyValueSendable;
import frc.robot.training.protocol.generic.StringSendable;

import org.frc.team5409.robot.Constants;
import org.frc.team5409.robot.training.robot.TrainerDashboard;
import org.frc.team5409.robot.training.robot.TrainingContext;
import org.frc.team5409.robot.training.robot.TrainingModel;

public class SubmitSetpointData extends CommandBase {
    private final TrainingContext _context;
    private final NetworkClient _client;
    private final TrainerDashboard _dashboard;

    private Future<NetworkResponse> _request;

    public SubmitSetpointData(TrainerDashboard dashboard, NetworkClient client, TrainingContext context) {
        _context = context;
        _client = client;
        _dashboard = dashboard;
        _request = null;
    }

    @Override
    public void initialize() {
        KeyValueSendable payload = new KeyValueSendable();
            payload.putSendable("trainer.topic", new StringSendable("trainer:submitData"));
            payload.putDouble("trainer.data.speed", _context.getSetpoint().getTarget() / Constants.Training.MAX_SPEED);
            payload.putDouble("trainer.data.distance", _context.getDistance() / Constants.Training.MAX_DISTANCE);

        _request = _client.submitRequestAsync(
            new NetworkRequest(payload)
        );

        System.out.println("Sent request");
    }

    @Override
    public void end(boolean interrupted) {    
        try {
            NetworkResponse response = _request.get();

            System.out.println("Received status : " + response.getStatus());
            if (response.getSendableResult() != null)
                System.out.println("Received payload : " + response.getSendableResult());

            if (response.getStatus() == NetworkStatus.STATUS_OK) {
                KeyValueSendable payload = (KeyValueSendable) response.getSendableResult();
            
                double modelA = payload.getDouble("trainer.model.parameters[0]");
                double modelB = payload.getDouble("trainer.model.parameters[1]");
                double modelC = payload.getDouble("trainer.model.parameters[2]");
                double modelD = payload.getDouble("trainer.model.parameters[3]");

                _context.setModel(new TrainingModel(modelA, modelB, modelC, modelD));
                _dashboard.update();
            } else {
                System.out.println("Received status : " + response.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve result of request", e);
        }
    }

    @Override
    public boolean isFinished() {
        return _request.isDone() || _request.isCancelled();
    }
}
