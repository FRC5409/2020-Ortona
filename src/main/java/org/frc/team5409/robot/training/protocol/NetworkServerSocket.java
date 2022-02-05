package org.frc.team5409.robot.training.protocol;

import java.io.*;
import java.net.ServerSocket;

public class NetworkServerSocket {
    public static final int DEFAULT_PORT = 5409;

    public static NetworkServerSocket create() throws IOException {
        return create(DEFAULT_PORT);
    }

    public static NetworkServerSocket create(int port) throws IOException {
        ServerSocket socket = new ServerSocket(port);

        return new NetworkServerSocket(socket);
    }

    private final ServerSocket _socket;

    protected NetworkServerSocket(ServerSocket socket) {
        _socket = socket;
    }

    public NetworkSocket accept() throws IOException {
        return NetworkSocket.create(_socket.accept());
    }

    public void close() throws IOException {
        _socket.close();
    }

    public boolean isClosed() {
        return _socket.isClosed();
    }
}
