package org.frc.team5409.robot.training.protocol;

import java.util.Map;

import org.jetbrains.annotations.Nullable;

public enum NetworkStatus {
    STATUS_OK(400),
    STATUS_ERROR(300),
    STATUS_UNAVAILABLE(404);

    @Nullable
    public static final NetworkStatus fromId(int id) {
        return STATUS_ID_MAP.get(id);
    }

    private static final Map<Integer, NetworkStatus> STATUS_ID_MAP = Map.of(
        STATUS_OK.id(), STATUS_OK,
        STATUS_ERROR.id(), STATUS_ERROR,
        STATUS_UNAVAILABLE.id(), STATUS_UNAVAILABLE
    );

    private final int _id;

    private NetworkStatus(int id) {
        _id = id;
    }

    public final int id() {
        return _id;
    }
}