package org.frc.team5409.robot.training.protocol;

import org.jetbrains.annotations.Nullable;

public class NetworkRequestResult {
    private final NetworkStatus _status;
    private final NetworkSendable _payload;

    public NetworkRequestResult(NetworkStatus status) {
        _status = status;
        _payload = null;
    }

    public NetworkRequestResult(NetworkStatus status, @Nullable NetworkSendable payload) {
        _status = status;
        _payload = payload;
    }

    public NetworkStatus getStatus() {
        return _status;
    }

    @Nullable
    public NetworkSendable getPayload() {
        return _payload;
    }
}
