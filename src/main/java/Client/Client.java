package Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Client {
    private static final int port = 8080;
    private String username;
    private SocketChannel channel;
    private String serverAddress;
    private Boolean connected; // connected to server?

    private Thread listenerThread;
    private Consumer<String> messageCallback;



    public Client(String username, String serverAddress, Consumer<String> callback) {
        this.username = username;
        this.serverAddress = serverAddress;
        this.messageCallback = callback;
        this.connected = false;
    }

    /**
     * Connects to serverAddress on port and returns messages to the provided callback
     */
    public void connect() throws IOException {
        if (connected) {
            throw new IllegalStateException("Already connected");
        }

        try {
            channel = SocketChannel.open();
            channel.socket().setSoTimeout(90);
            channel.connect(new InetSocketAddress(serverAddress, port));
            connected = true;
            notifyMessage("Connected.");
            listenerThread = new Thread(this::listen);
            listenerThread.setDaemon(true); // Won't prevent JVM shutdown
            listenerThread.start();
        } catch (IOException e) {
            notifyMessage("Exception: " + e.getMessage());
            throw e;
        }
    }

    private void listen() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (connected && channel != null && channel.isOpen()) {
            try {
                buffer.clear();
                int bytesRead = channel.read(buffer);

                if (bytesRead == -1) {
                    // Server closed connection
                    notifyMessage("Server closed connection");
                    break;
                }

                if (bytesRead > 0) {
                    buffer.flip();
                    String message = StandardCharsets.UTF_8.decode(buffer).toString();

                    try {
                        notifyMessage(message);
                    } catch (Exception e) {
                        notifyMessage("Failed to parse message: " + e.getMessage());
                    }
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

    private void notifyMessage(String message) {
        if (messageCallback != null) {
            messageCallback.accept(message);
        }
    }

    public void sendMessage(String message) throws IOException {
        if (!connected || channel == null) {
            throw new IllegalStateException("Not connected to server");
        }

        try {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
            channel.write(buffer);
        } catch (IOException e) {
            throw e;
        }
    }


}
