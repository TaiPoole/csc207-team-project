package client;

import common.Message;
import common.TextMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * Client that communicates with server - sends and receives Message.
 */
public class Client {
    private static final int PORT = 8080;
    private final String username;
    private SocketChannel channel;
    private final String serverAddress;
    private Boolean connected; // connected to server?

    private final Consumer<Message> messageCallback;

    public Client(String username, String serverAddress, Consumer<Message> callback) {
        this.username = username;
        this.serverAddress = serverAddress;
        this.messageCallback = callback;
        this.connected = false;
    }

    /**
     * Connects to serverAddress on port and returns messages to the provided callback.
     */
    public void connect() throws IOException {
        if (connected) {
            throw new IllegalStateException("Already connected");
        }

        channel = SocketChannel.open();
        channel.socket().setSoTimeout(90);
        channel.connect(new InetSocketAddress(serverAddress, PORT));
        connected = true;
        Thread listenerThread = new Thread(this::listen);
        listenerThread.setDaemon(true); // Won't prevent JVM shutdown
        listenerThread.start();
    }

    private void listen() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (connected && channel != null && channel.isOpen()) {
            try {
                buffer.clear();
                int bytesRead = channel.read(buffer);

                if (bytesRead == -1) {
                    // Server closed connection
                    break;
                }

                if (bytesRead > 0) {
                    buffer.flip();
                    String s_message = StandardCharsets.UTF_8.decode(buffer).toString();
                    String[] parts = s_message.split("\n", 2);

                    if (parts.length < 2) {
                        throw new IllegalArgumentException("Invalid format");
                    }

                    notifyMessage(deserializeMessage(parts[0], parts[1]));
                }
                Thread.sleep(10);

            } catch (InterruptedException e) {
                // Thread was interrupted, exit gracefully
                break;
            } catch (IOException e) {
                break;
            }
        }
    }

    private void notifyMessage(Message message) {
        if (messageCallback != null && message != null) {
            messageCallback.accept(message);
        }
    }

    private Message deserializeMessage(String className, String serializedData) {
        try {
            // Extract simple class name if it's a fully qualified name
            String simpleClassName = className;
            if (className.contains(".")) {
                simpleClassName = className.substring(className.lastIndexOf('.') + 1);
            }

            // Handle known message types
            switch (simpleClassName) {
                case "textMessage":
                    return TextMessage.deserialize(serializedData);
                default:
                    System.err.println("Unknown message type: " + className);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error deserializing message: " + e.getMessage());
            return null;
        }
    }

    public void sendMessage(String message) throws IOException {
        sendMessage(new TextMessage(username, message, LocalDateTime.now()));
    }

    public void sendMessage(Message message) throws IOException {
        if (!connected || channel == null) {
            throw new IllegalStateException("Not connected to server");
        }

        String serializedMessage = message.getClass().getName() + "\n" + message.serialize();
        ByteBuffer buffer = ByteBuffer.wrap(serializedMessage.getBytes(StandardCharsets.UTF_8));
        channel.write(buffer);
    }

    public String getUsername() {
        return username;
    }
}
