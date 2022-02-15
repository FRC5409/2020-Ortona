package org.frc.team5409.robot.training.protocol;

import java.util.concurrent.*;

public class NetworkExecutors {
    private static final NetworkExecutors _instance = new NetworkExecutors();

    public static NetworkExecutors getInstance() {
        return _instance;
    }

    private final ExecutorService _executor;

    private NetworkExecutors() {
        _executor = Executors.newCachedThreadPool();
    }

    public void submit(RunnableFuture<?> future) {
        _executor.submit(future);
    }
}
