package server;

import java.nio.channels.SocketChannel;

public class User {
    private final String username;
    private final SocketChannel socketChannel;

    public User(String username, SocketChannel socketChannel) {
        this.username = username;
        this.socketChannel = socketChannel;
    }

    public SocketChannel getChannel() {
        return socketChannel;
    }

    public String getUsername() {
        return username;
    }
}
