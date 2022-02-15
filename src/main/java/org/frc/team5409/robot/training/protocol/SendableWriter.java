package org.frc.team5409.robot.training.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

public class SendableWriter {
    private final SendableContext _context;
    private final DataOutputStream _stream;

    public SendableWriter(SendableContext context, DataOutputStream stream) {
        _context = context;
        _stream = stream;
    }

    public void write(NetworkSendable sendable) throws IOException {
        _stream.writeLong(sendable.what());
        sendable.write(_context, _stream);
    }

    public void flush() throws IOException {
        _stream.flush();
    }
}
