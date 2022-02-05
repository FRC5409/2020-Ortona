package org.frc.team5409.robot.training.protocol;

import org.frc.team5409.robot.training.util.Factory;

final class SendableRegistryEntry<T extends NetworkSendable> {
    public final Class<T> type; 
    public final Factory<T> factory;

    public SendableRegistryEntry(Class<T> type, Factory<T> factory) {
        this.type = type;
        this.factory = factory;
    }
}