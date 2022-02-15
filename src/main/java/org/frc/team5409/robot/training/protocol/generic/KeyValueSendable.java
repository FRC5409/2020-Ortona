package org.frc.team5409.robot.training.protocol.generic;

import org.frc.team5409.robot.training.collections.Entry;
import org.frc.team5409.robot.training.collections.EntryIterable;
import org.frc.team5409.robot.training.collections.EntryIterator;
import org.frc.team5409.robot.training.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Inspired by android bundles
public class KeyValueSendable implements NetworkSendable, EntryIterable<String, NetworkSendable> {
    @SuppressWarnings("unused")
    private static void register(SendableRegistryBuilder registry) {
        registry.registerFactory(KeyValueSendable.WHAT, KeyValueSendable.class, KeyValueSendable::new);
        registry.registerSendable(ValueSendable.class);
        registry.registerSendable(StringSendable.class);
    }

    public static final long WHAT = 4019083808795941718L;

    private static final byte STREAM_COLLECTION_ITEM = 0x1;
    private static final byte STREAM_COLLECTION_END = 0x2;

    protected Map<String, NetworkSendable> _values;

    public KeyValueSendable() {
        _values = new HashMap<>();
    }

    public KeyValueSendable(KeyValueSendable copy) {
        _values = new HashMap<>(copy._values);
    }

    public void putBoolean(@NotNull String name, boolean value) {
        putValue(name, value);
    }

    public void putByte(@NotNull String name, byte value) {
        putValue(name, value);
    }

    public void putCharacter(@NotNull String name, char value) {
        putValue(name, value);
    }

    public void putDouble(@NotNull String name, double value) {
        putValue(name, value);
    }

    public void putFloat(@NotNull String name, float value) {
        putValue(name, value);
    }

    public void putInteger(@NotNull String name, int value) {
        putValue(name, value);
    }

    public void putLong(@NotNull String name, long value) {
        putValue(name, value);
    }

    public void putShort(@NotNull String name, short value) {
        putValue(name, value);
    }

    public void putSendable(@NotNull String name, @NotNull NetworkSendable value) {
        _values.put(name, value);
    }

    public boolean getBoolean(@NotNull String name) {
        return getValue(name, Boolean.class);
    }

    public byte getByte(@NotNull String name) {
        return getValue(name, Byte.class);
    }

    public char getCharacter(@NotNull String name) {
        return getValue(name, Character.class);
    }

    public double getDouble(@NotNull String name) {
        return getValue(name, Double.class);
    }

    public float getFloat(@NotNull String name) {
        return getValue(name, Float.class);
    }

    public int getInteger(@NotNull String name) {
        return getValue(name, Integer.class);
    }

    public long getLong(@NotNull String name) {
        return getValue(name, Long.class);
    }

    public short getShort(@NotNull String name) {
        return getValue(name, Short.class);
    }

    public NetworkSendable getSendable(@NotNull String name) {
        checkKey(name);
        return _values.get(name);
    }

    public void clear() {
        _values.clear();
    }

    public int size() {
        return _values.size();
    }

    public boolean exists(@NotNull String name) {
        return _values.containsKey(name);
    }

    protected void putValue(String name, Object value) {
        _values.put(name, new ValueSendable(value));
    }

    protected <T> T getValue(String name, Class<T> clazz) throws IllegalArgumentException, ClassCastException {
        checkKey(name);

        Object rawValue = _values.get(name);
        if (!ValueSendable.class.isAssignableFrom(rawValue.getClass()))
            throw new IllegalArgumentException("Cannot access key '" + name +
                "' with ValueSendable semantics, class '" + rawValue.getClass().getSimpleName() + "' cannot be casted to 'ValueSendable'");

        ValueSendable valueSendable = (ValueSendable) rawValue;

        if (valueSendable.getType() == null)
            throw new ClassCastException("ValueSendable with name '" + name + "' cannot be retrieved with null type ");
        else if (!clazz.isAssignableFrom(valueSendable.getType()))
            throw new ClassCastException("ValueSendable with name '" + name + "' and type '" +
                valueSendable.getType().getSimpleName() + "' cannot be casted to '" + clazz.getSimpleName() + "'");

        return clazz.cast(valueSendable.getValue());
    }

    private void checkKey(String name) {
        if (!_values.containsKey(name))
            throw new IllegalArgumentException("Key '" + name + "' does not exist");
    }

    @NotNull
    @Override
    public EntryIterator<String, NetworkSendable> iterator() {
        return new EntryIterator<>(_values);
    }

    @Override
    public long what() {
        return WHAT;
    }

    @Override
    public void read(SendableContext context, DataInputStream stream) throws IOException {
        SendableReader reader = new SendableReader(context, stream);

        _values.clear();

        byte status;
        while ((status = stream.readByte()) != STREAM_COLLECTION_END) {
            if (status != STREAM_COLLECTION_ITEM)
                throw new IOException("Malformed content");

            StringSendable key = (StringSendable) reader.read(StringSendable.WHAT);
            NetworkSendable value = reader.read();

            _values.put(key.getValue(), value);
        }
    }

    @Override
    public void write(SendableContext context, DataOutputStream stream) throws IOException {
        SendableWriter writer = new SendableWriter(context, stream);

        StringSendable key = new StringSendable();
        for (Entry<String, NetworkSendable> entry : this) {
            stream.writeByte(STREAM_COLLECTION_ITEM);
            key.setValue(entry.key());

            writer.write(key);
            writer.write(entry.value());
        }

        stream.writeByte(STREAM_COLLECTION_END);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("KeyValueSendable[" + size() + "]{\n");

        if (size() > 0) {
            for (Entry<String, NetworkSendable> entry : this) {
                builder.append("\t\"");
                builder.append(entry.key());
                builder.append("\" = ");
                builder.append(entry.value());
                builder.append(",\n");
            }

            builder.setCharAt(builder.length() - 2, ' ');
        }

        builder.append("}");

        return builder.toString();
    }
}