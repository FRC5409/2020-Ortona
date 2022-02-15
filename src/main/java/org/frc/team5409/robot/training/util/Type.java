package org.frc.team5409.robot.training.util;

import org.frc.team5409.robot.training.collections.Mapping;
import org.jetbrains.annotations.Nullable;

public class Type {
    private static final Mapping<Class<?>, Class<?>> PRIMITIVE_MAP = Mapping.of(
        char.class, Character.class,
        short.class, Short.class,
        int.class, Integer.class,
        long.class, Long.class,
        boolean.class, Boolean.class
    );

    public static boolean isPrimitiveType(Class<?> type) {
        return PRIMITIVE_MAP.contains(type);
    }

    public static boolean isUnwrappedType(Class<?> type) {
        return PRIMITIVE_MAP.containsForward(type);
    }
    public static boolean isWrappedType(Class<?> type) {
        return PRIMITIVE_MAP.containsReverse(type);
    }

    @Nullable
    public static Class<?> getWrappedType(Class<?> type) {
        return PRIMITIVE_MAP.forward(type);
    }

    @Nullable
    public static Class<?> getUnwrappedType(Class<?> type) {
        return PRIMITIVE_MAP.reverse(type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Object object) {
        return (T) object;
    }
}
