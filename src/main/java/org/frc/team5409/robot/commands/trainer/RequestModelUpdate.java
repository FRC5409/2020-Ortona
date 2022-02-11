package org.frc.team5409.robot.commands.trainer;

import java.util.concurrent.Future;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.training.protocol.NetworkClient;
import frc.robot.training.protocol.NetworkStatus;
import frc.robot.training.protocol.NetworkRequest;
import frc.robot.training.protocol.NetworkResponse;
import frc.robot.training.protocol.generic.KeyValueSendable;
import frc.robot.training.protocol.generic.StringSendable;
import org.frc.team5409.robot.training.robot.TrainingContext;
import org.frc.team5409.robot.training.robot.TrainingModel;

public class RequestModelUpdate extends CommandBase {
    private final TrainingContext _context;
    private final NetworkClient _client;

    private Future<NetworkResponse> _request;

    public RequestModelUpdate(NetworkClient client, TrainingContext context) {
        _context = context;
        _client = client;
        _request = null;
    }

    @Override
    public void initialize() {
        KeyValueSendable payload = new KeyValueSendable();
            payload.putSendable("trainer.topic", new StringSendable("trainer.get-model"));

        _request = _client.submitRequestAsync(
            new NetworkRequest(payload)
        );
    }

    @Override
    public void end(boolean interrupted) {    
        try {
            NetworkResponse response = _request.get();
            if (response.getStatus() == NetworkStatus.STATUS_OK) {
                KeyValueSendable payload = (KeyValueSendable) response.getSendableResult();
                StringSendable topic = (StringSendable) payload.getSendable("trainer.topic");

                if (topic.getValue().equals("trainer:submitData")) {
                    double modelA = payload.getDouble("trainer.model.parameters[3]");
                    double modelB = payload.getDouble("trainer.model.parameters[2]");
                    double modelC = payload.getDouble("trainer.model.parameters[1]");
                    double modelD = payload.getDouble("trainer.model.parameters[0]");

                    _context.setModel(new TrainingModel(modelA, modelB, modelC, modelD));
                }

                System.out.println("Received payload : " + payload);
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
