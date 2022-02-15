package org.frc.team5409.robot.commands.trainer;

import java.util.concurrent.Future;

import edu.wpi.first.wpilibj2.command.CommandBase;
import org.frc.team5409.robot.training.protocol.NetworkClient;
import org.frc.team5409.robot.training.protocol.NetworkStatus;
import org.frc.team5409.robot.training.protocol.NetworkTransaction;
import org.frc.team5409.robot.training.protocol.NetworkTransactionResult;
import org.frc.team5409.robot.training.protocol.generic.KeyValueSendable;
import org.frc.team5409.robot.training.protocol.generic.StringSendable;
import org.frc.team5409.robot.training.robot.TrainingContext;
import org.frc.team5409.robot.training.robot.TrainingModel;

public class SubmitSetpointData extends CommandBase {
    private final TrainingContext _context;
    private final NetworkClient _client;

    private Future<NetworkTransactionResult> _request;

    public SubmitSetpointData(NetworkClient client, TrainingContext context) {
        _context = context;
        _client = client;
        _request = null;
    }

    @Override
    public void initialize() {
        KeyValueSendable payload = new KeyValueSendable();
            payload.putSendable("trainer.topic", new StringSendable("trainer.setpoint"));
            payload.putDouble("trainer.setpoint.target", _context.getTargetSetpoint().getTarget());
            payload.putDouble("trainer.setpoint.distance", _context.getDistance());

        _request = _client.submitTransactionAsync(
            new NetworkTransaction(payload)
        );
    }

    @Override
    public void end(boolean interrupted) {    
        try {
            NetworkTransactionResult result = _request.get();
            if (result.getStatus() == NetworkStatus.STATUS_OK) {
                KeyValueSendable payload = (KeyValueSendable) result.getSendableResult();
                StringSendable topic = (StringSendable) payload.getSendable("trainer.topic");

                if (topic.getValue().equals("trainer.update-model")) {
                    double modelA = payload.getDouble("trainer.model.kA");
                    double modelB = payload.getDouble("trainer.model.kB");
                    double modelC = payload.getDouble("trainer.model.kC");
                    double modelD = payload.getDouble("trainer.model.kD");

                    _context.setModel(new TrainingModel(modelA, modelB, modelC, modelD));
                }

                System.out.println("Received payload : " + payload);
            } else {
                System.out.println("Received status : " + result.getStatus());
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
