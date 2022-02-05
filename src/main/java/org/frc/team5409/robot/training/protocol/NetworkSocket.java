package org.frc.team5409.robot.training.protocol;

import java.io.*;
import java.net.Socket;

public class NetworkSocket {
    public static final int DEFAULT_PORT = 5409;
    public static final String DEFAULT_ADDRESS = "127.0.0.1";

    public static NetworkSocket create() throws IOException {
        return create(DEFAULT_ADDRESS, DEFAULT_PORT);
    }

    public static NetworkSocket create(String address) throws IOException {
        return create(address, DEFAULT_PORT);
    }

    public static NetworkSocket create(String address, int port) throws IOException {
        return create(new Socket(address, port));
    }

    public static NetworkSocket create(Socket socket) throws IOException {
        DataInputStream inputStream = new DataInputStream(
            new BufferedInputStream(socket.getInputStream())
        );

        DataOutputStream outputStream = new DataOutputStream(
            new BufferedOutputStream(socket.getOutputStream())
        );

        return new NetworkSocket(socket, inputStream, outputStream);
    }
    private final Socket _socket;
    private final DataInputStream _input;
    private final DataOutputStream _output;

    protected NetworkSocket(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) {
        _socket = socket;
        _input = inputStream;
        _output  = outputStream;
    }

    public DataInputStream getInputStream() throws IOException {
        if (_socket.isClosed())
            throw new IOException("Cannot get input stream of closed socket");
        return _input;
    }

    public DataOutputStream getOutputStream() throws IOException {
        if (_socket.isClosed())
            throw new IOException("Cannot get output stream of closed socket");
        return _output;
    }

    public void close() throws IOException {
        _socket.close();
    }

    public boolean isClosed() {
        return _socket.isClosed();
    }

    public boolean isConnected() {
        return _socket.isConnected();
    }
}
