package org.frc.team5409.robot.training.protocol.generic;

import org.frc.team5409.robot.training.protocol.NetworkSendable;
import org.frc.team5409.robot.training.protocol.SendableContext;
import org.frc.team5409.robot.training.protocol.SendableRegistryBuilder;
import org.frc.team5409.robot.training.util.Type;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class ValueSendable implements NetworkSendable {
    @SuppressWarnings("unused")
    private static void register(SendableRegistryBuilder registry) {
        registry.registerFactory(ValueSendable.WHAT, ValueSendable.class, ValueSendable::new);
    }

    public static final long WHAT = -7383229974325473331L;

    public static final byte DATA_TYPE_NULL    = -1;
    public static final byte DATA_TYPE_BYTE    = 0x0;
    public static final byte DATA_TYPE_CHAR    = 0x1;
    public static final byte DATA_TYPE_SHORT   = 0x2;
    public static final byte DATA_TYPE_INT     = 0x3;
    public static final byte DATA_TYPE_FLOAT   = 0x4;
    public static final byte DATA_TYPE_LONG    = 0x5;
    public static final byte DATA_TYPE_DOUBLE  = 0x6;
    public static final byte DATA_TYPE_BOOLEAN = 0x7;

    private static final Map<Class<?>, Byte> DATA_TYPE_MAP = Map.of(
        Byte.class, DATA_TYPE_BYTE,
        Character.class, DATA_TYPE_CHAR,
        Short.class, DATA_TYPE_SHORT,
        Integer.class, DATA_TYPE_INT,
        Float.class, DATA_TYPE_FLOAT,
        Long.class, DATA_TYPE_LONG,
        Double.class, DATA_TYPE_DOUBLE,
        Boolean.class, DATA_TYPE_BOOLEAN
    );

    protected Object   _value;
    protected Class<?> _type;
    protected byte     _datatype;

    public ValueSendable() {
        _value = null;
        _type  = Object.class;
        _datatype = DATA_TYPE_NULL;
    }

    public ValueSendable(Object value) {
        setValue(value);
    }

    public ValueSendable(ValueSendable copy) {
        _value = copy._value;
        _type = copy._type;
        _datatype = copy._datatype;
    }

    @Override
    public long what() {
        return WHAT;
    }

    @Override
    public void read(SendableContext context, DataInputStream stream) throws IOException {
        byte datatype = stream.readByte();

        switch(datatype) {
            case DATA_TYPE_BYTE:
                _value = stream.readByte();
                _type = Byte.class;
                break;
            case DATA_TYPE_CHAR:
                _value = stream.readChar();
                _type = Character.class;
                break;
            case DATA_TYPE_SHORT:
                _value = stream.readShort();
                _type = Short.class;
                break;
            case DATA_TYPE_INT:
                _value = stream.readInt();
                _type = Integer.class;
                break;
            case DATA_TYPE_FLOAT:
                _value = stream.readFloat();
                _type = Float.class;
                break;
            case DATA_TYPE_LONG:
                _value = stream.readLong();
                _type = Long.class;
                break;
            case DATA_TYPE_DOUBLE:
                _value = stream.readDouble();
                _type = Double.class;
                break;
            case DATA_TYPE_BOOLEAN:
                _value = stream.readBoolean();
                _type = Boolean.class;
                break;
            default:
                throw new IOException("Malformed Data. Unexpected datatype 0x"+Integer.toHexString(datatype));
        }

        _datatype = datatype;
    }

    @Override
    public void write(SendableContext context, DataOutputStream stream) throws IOException {
        if (_value == null)
            throw new IOException("Cannot write 'null' value.");

        stream.writeByte(_datatype);

        switch(_datatype) {
            case DATA_TYPE_BYTE:
                stream.writeByte((Integer) _value);
                break;
            case DATA_TYPE_CHAR:
                stream.writeChar((Integer) _value);
                break;
            case DATA_TYPE_SHORT:
                stream.writeShort((Integer) _value);
                break;
            case DATA_TYPE_INT:
                stream.writeInt((Integer) _value);
                break;
            case DATA_TYPE_FLOAT:
                stream.writeFloat((Float) _value);
                break;
            case DATA_TYPE_LONG:
                stream.writeLong((Long) _value);
                break;
            case DATA_TYPE_DOUBLE:
                stream.writeDouble((Double) _value);
                break;
            case DATA_TYPE_BOOLEAN:
                stream.writeBoolean((Boolean) _value);
                break;
        }
    }

    public void setValue(Object value) {
        Class<?> type = value.getClass();

        if (!Type.isPrimitiveType(type))
            throw new IllegalArgumentException("Cannot apply non primitive type '" + type.getSimpleName() + "'");

        if (Type.isUnwrappedType(type))
            type = Type.getUnwrappedType(type);

        _value = value;
        _type = type;
        _datatype = Objects.requireNonNull(DATA_TYPE_MAP.get(type));
    }

    @Nullable
    public Object getValue() {
        return _value;
    }

    @Nullable
    public <T> T getValue(Class<T> type) {
        if (!type.isAssignableFrom(_type))
            throw new IllegalArgumentException("Cannot cast value type '" + _type.getSimpleName() + "' to type '" + _type.getSimpleName() + "'");
        return type.cast(_value);
    }

    @Nullable
    public Class<?> getType() {
        return _type;
    }

    @Override
    public String toString() {
        return "ValueSendable<" + _type.getSimpleName() + ">(" + _value + ")";
    }
}