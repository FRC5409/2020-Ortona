package org.frc.team5409.robot.training.protocol.generic;

import org.frc.team5409.robot.training.protocol.NetworkSendable;
import org.frc.team5409.robot.training.protocol.SendableContext;
import org.frc.team5409.robot.training.protocol.SendableRegistryBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StringSendable implements NetworkSendable {
    @SuppressWarnings("unused")
    private static void register(SendableRegistryBuilder registry) {
        registry.registerFactory(StringSendable.WHAT, StringSendable.class, StringSendable::new);
    }

    public static final long WHAT = 3273615128644272490L;

    private static final byte EOF = '\0';
    private static final int BUFFER_SIZE = 1204;

    private String _value;

    public StringSendable() {
        _value = null;
    }

    public StringSendable(String value) {
        _value = value;
    }

    public StringSendable(StringSendable copy) {
        _value = copy._value;
    }

    @Override
    public long what() {
        return WHAT;
    }

    @Override
    public void read(SendableContext context, DataInputStream stream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        int i = 0;
        int k = 0;

        byte b;
        while ((b = stream.readByte()) != EOF) {
            if (i == BUFFER_SIZE) {
                byte[] temp = new byte[buffer.length + BUFFER_SIZE];
                System.arraycopy(buffer, 0, temp, 0, buffer.length);
                buffer = temp;
                i = 0;
            }

            buffer[k] = b;

            i++;
            k++;
        }

        _value = new String(buffer, 0, k, StandardCharsets.US_ASCII);
    }

    @Override
    public void write(SendableContext context, DataOutputStream stream) throws IOException {
        if (_value == null)
            throw new IOException("Cannot write 'null' value.");

        stream.write(_value.getBytes(StandardCharsets.US_ASCII));
        stream.writeByte(EOF);
    }

    public void setValue(@NotNull String value) {
        _value = value;
    }

    @Nullable
    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return "StringSendable(\"" + _value + "\")";
    }
}
