package org.frc.team5409.robot.training.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface NetworkSendable {
    long what();
    void read(SendableContext context, DataInputStream stream) throws IOException;
    void write(SendableContext context, DataOutputStream stream) throws IOException;
}
