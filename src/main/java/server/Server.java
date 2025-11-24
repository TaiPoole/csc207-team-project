package server;

import common.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ArrayList<common.Channel> channels; // server channels, not used for communication
    private final Map<String, User> connectedUsers;
    private final Map<SocketChannel, User> channelToUser; // Map channel to user

    private final int port;
    private ServerSocketChannel serverChannel;
    private final ExecutorService clientHandlerPool;
    private Thread acceptThread;


    public Server(int port) {
        this.channels = new ArrayList<>();
        this.port = port;
        this.connectedUsers = new HashMap<>();
        this.channelToUser = new HashMap<>();
        this.clientHandlerPool = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        System.out.println("Server started on port " + port);

        acceptThread = new Thread(this::acceptClients);
        acceptThread.start();
    }

    private void acceptClients() {
        while (true) {
            try {
                SocketChannel clientChannel = serverChannel.accept();
                String clientAddress = clientChannel.getRemoteAddress().toString();
                System.out.println("New client connected: " + clientAddress);

                clientHandlerPool.execute(() -> handleClient(clientChannel, clientAddress));

            } catch (IOException e) {
                System.err.println("Error accepting client: " + e.getMessage());
            }
        }
    }

    private void handleClient(SocketChannel clientChannel, String clientAddress) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        User user = null;

        try {
            while (clientChannel.isOpen()) {
                buffer.clear();
                int bytesRead = clientChannel.read(buffer);

                if (bytesRead == -1) {
                    // Client disconnected
                    break;
                }

                if (bytesRead > 0) {
                    buffer.flip();
                    String receivedData = StandardCharsets.UTF_8.decode(buffer).toString();

                    // Parse the message: className\nserializedData
                    String[] parts = receivedData.split("\n", 2);

                    if (parts.length < 2) {
                        System.err.println("Invalid message format from " + clientAddress);
                        continue;
                    }

                    String messageClassName = parts[0];
                    String serializedData = parts[1];

                    // Deserialize the message
                    Message message = deserializeMessage(messageClassName, serializedData);

                    if (message != null) {
                        // Extract username from the message itself
                        String username = message.getUsername();

                        // Register user on first message
                        if (user == null && username != null) {
                            user = new User(username, clientChannel);
                            connectedUsers.put(username, user);
                            channelToUser.put(clientChannel, user);
                            System.out.println("User registered: " + username);
                        }

                        //Handle server-side logic based on message type
                        switch (message.getClass().getSimpleName()) {

                            case "CreateChannelMessage":
                                handleCreateChannel(clientChannel, (CreateChannelMessage) message);
                                continue; // Do NOT broadcast system messages
                        }

                        // Broadcast to all other clients
                        if (user != null) {
                            broadcastMessage(message, user.getUsername());
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client " + clientAddress + ": " + e.getMessage());
        } finally {
            // Clean up when client disconnects
            if (user != null) {
                connectedUsers.remove(user.getUsername());
                channelToUser.remove(clientChannel);
                System.out.println("User disconnected: " + user.getUsername());
            }
            try {
                clientChannel.close();
            } catch (IOException e) {
                System.err.println("Error closing channel: " + e.getMessage());
            }
        }
    }

    private Message deserializeMessage(String className, String serializedData) {
        try {
            // Extract simple class name if it's a fully qualified name
            String simpleClassName = className;
            if (className.contains(".")) {
                simpleClassName = className.substring(className.lastIndexOf('.') + 1);
            }

            // Remove "class " prefix if present
            simpleClassName = simpleClassName.replace("class ", "");

            // Handle known message types
            switch (simpleClassName) {
                case "TextMessage":
                    return TextMessage.deserialize(serializedData);
                // Add other message types here as needed
                case "CreateChannelMessage":
                    return CreateChannelMessage.deserialize(serializedData);
                case "ChannelCreationSuccessMessage":
                    return ChannelCreationSuccessMessage.deserialize(serializedData);

                case "ChannelCreationErrorMessage":
                    return ChannelCreationErrorMessage.deserialize(serializedData);
                default:
                    System.err.println("Unknown message type: " + className);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error deserializing message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Handles the logic for creating a new channel on the server.
     * Checks if the channel already exists, adds it if not, and sends
     * the appropriate success or error response back to the client.
     *
     * @param clientChannel The channel of the requesting client
     * @param message       The received CreateChannelMessage
     */
    private void handleCreateChannel(SocketChannel clientChannel, CreateChannelMessage message) throws IOException {
        String name = message.getChannelName();

        synchronized (channels) {
            boolean exists = channels.stream()
                    .anyMatch(ch -> ch.getId().equals(name));

            if (exists) {
                // Send FAILED response
                sendToClient(clientChannel,
                        new ChannelCreationErrorMessage(
                                message.getUsername(),
                                "Channel '" + name + "' already exists."
                        ));
            } else {
                // Create new channel
                common.Channel newChannel = new common.Channel(name);
                addChannel(newChannel);

                // Send SUCCESS response
                sendToClient(clientChannel,
                        new ChannelCreationSuccessMessage(
                                message.getUsername(),
                                name
                        ));
            }
        }
    }


    /**
     * Broadcast a message to all connected clients except the sender
     */
    private void broadcastMessage(Message message, String senderUsername) {
        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            String username = entry.getKey();
            User user = entry.getValue();

            // Don't send message back to sender
            if (username.equals(senderUsername)) {
                continue;
            }

            try {
                sendToClient(user.getChannel(), message);
            } catch (IOException e) {
                System.err.println("Error sending to " + username + ": " + e.getMessage());
            }
        }
    }

    /**
     * Send a message to a specific client using the protocol format
     */
    public void sendToClient(SocketChannel clientChannel, Message message) throws IOException {
        if (!clientChannel.isOpen()) {
            throw new IOException("Channel is closed");
        }

        // Format: className\nserializedData
        String serializedMessage = message.getClass().getName() + "\n" + message.serialize();

        ByteBuffer buffer = ByteBuffer.wrap(serializedMessage.getBytes(StandardCharsets.UTF_8));
        clientChannel.write(buffer);
    }

    /**
     * Broadcast a message to all connected clients
     */
    public void sendToAll(Message message) throws IOException {
        ArrayList<String> disconnectedUsers = new ArrayList<>();

        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            String username = entry.getKey();
            User user = entry.getValue();
            SocketChannel channel = user.getChannel();

            if (channel.isOpen()) {
                try {
                    sendToClient(channel, message);
                } catch (IOException e) {
                    System.err.println("Error sending to " + username + ": " + e.getMessage());
                    disconnectedUsers.add(username);
                }
            } else {
                disconnectedUsers.add(username);
            }
        }

        // Clean up disconnected users
        for (String username : disconnectedUsers) {
            User userToRemove = connectedUsers.remove(username);
            if (userToRemove != null) {
                channelToUser.remove(userToRemove.getChannel());
            }
            System.out.println("Removed disconnected user: " + username);
        }
    }

    public void addChannel(common.Channel channel) {
        channels.add(channel);
    }

    public void shutdown() {
        try {
            if (serverChannel != null && serverChannel.isOpen()) {
                serverChannel.close();
            }
            clientHandlerPool.shutdown();
        } catch (IOException e) {
            System.err.println("Error shutting down server: " + e.getMessage());
        }
    }
}