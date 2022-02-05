package org.frc.team5409.robot.training.protocol;

import org.frc.team5409.robot.training.util.Factory;

import java.io.DataInputStream;
import java.io.IOException;

public class SendableReader {
    private final SendableContext _context;
    private final DataInputStream _stream;

    public SendableReader(SendableContext context, DataInputStream stream) {
        _context = context;
        _stream = stream;
    }

    public NetworkSendable read() throws IOException {
        long what = _stream.readLong();

        Factory<? extends NetworkSendable> sendableFactory = _context.getRegistry().getSendableFactory(what);
        if (sendableFactory == null)
            throw new IOException("Unexpected 'what' in sendable, got 0x"+Long.toHexString(what));

        NetworkSendable sendable = sendableFactory.create();
        sendable.read(_context, _stream);

        return sendable;
    }

    public NetworkSendable read(long what) throws IOException {
        long readWhat = _stream.readLong();

        Factory<? extends NetworkSendable> sendableFactory = _context.getRegistry().getSendableFactory(what);
        if (sendableFactory == null)
            throw new IOException("Unexpected 'what' in sendable, got 0x" + Long.toHexString(what));
        else if (readWhat != what)
            throw new IOException("Unexpected 'what' in sendable, expected 0x" +
                Long.toHexString(what) + ", got 0x" + Long.toHexString(what));

        NetworkSendable sendable = sendableFactory.create();
        sendable.read(_context, _stream);

        return sendable;
    }
}
