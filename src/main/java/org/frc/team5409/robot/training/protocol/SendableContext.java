package org.frc.team5409.robot.training.protocol;

public class SendableContext {
    private final SendableRegistry _registry;

    public SendableContext() {
        _registry = new SendableRegistry();
    }

    public void registerSendable(Class<? extends NetworkSendable> type) {
        _registry.registerSendable(type);
    }

    public SendableRegistry getRegistry() {
        return _registry;
    }
}
