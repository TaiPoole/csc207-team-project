package server;

import common.AttachmentMessage;
import common.ManagePermissionMessage;
import common.Message;
import common.Permission;
import common.TextMessage;
import common.User;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class.
 * Centrally manages the messages between clients.
 * Holds channels, maps channels to users in channels, as well as server info like port, serverChannel,
 * and a handler pool for connected clients to the service.
 **/
public class Server {
    private final ArrayList<common.Channel> channels; // server channels, not used for communication
    private final Map<SocketChannel, User> channelToUser; // Map channel to user (primary mapping)
    private final Map<String, SocketChannel> usernameToChannel; // Reverse lookup for convenience
    private final Map<String, String> userCurrentChannel; // Track which channel each user is currently in

    private final int port;
    private final ExecutorService clientHandlerPool;
    private final PermissionManager permissionManager;
    private ServerSocketChannel serverChannel;

    /**
     * Server constructor.
     * Initializes server with empty variables (except for port)
     *
     * @param port the port for the server to listen on (default 8080 in our program)
     */
    public Server(int port) {
        this.channels = new ArrayList<>();
        this.port = port;
        this.channelToUser = new ConcurrentHashMap<>();
        this.usernameToChannel = new ConcurrentHashMap<>();
        this.userCurrentChannel = new ConcurrentHashMap<>();
        this.clientHandlerPool = Executors.newCachedThreadPool();
        this.permissionManager = new PermissionManager();

        // Create a default "general" channel
        addChannel(new common.Channel("general"));
    }

    /**
     * Server runtime.
     *
     * @param args default main args, not used
     */
    public static void main(String[] args) {
        Server server = new Server(8080);
        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Unable to start server, error: " + e.getMessage());
        }
    }

    /**
     * Starts the server.
     * Binds the socket and starts listening for clients.
     *
     * @throws IOException if an I/O error occurs with opening the socket
     */
    public void start() throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        System.out.println("Server started on port " + port);

        Thread acceptThread = new Thread(this::acceptClients);
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
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
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
                            user = new User(username);

                            // If this is the first user, grant them admin permissions in all channels
                            if (channelToUser.isEmpty()) {
                                // Grant permissions in all channels (including general)
                                for (common.Channel channel : channels) {
                                    permissionManager.addPermission(user, channel.getId(), Permission.EDIT_PERMISSIONS);
                                    permissionManager.addPermission(user, channel.getId(), Permission.WRITE);
                                    permissionManager.addPermission(user, channel.getId(), Permission.JOIN);
                                }
                            } else {
                                permissionManager.addPermission(user, "general", Permission.WRITE);
                            }

                            channelToUser.put(clientChannel, user);
                            usernameToChannel.put(username, clientChannel);
                            userCurrentChannel.put(username, "general"); // Default to general channel

                            System.out.println("User registered: " + username);
                            sendSystemMessage(clientChannel, "Welcome! You are in channel: general");
                        }

                        if (user != null) {
                            String currentChannel = userCurrentChannel.get(user.getUsername());

                            if (message instanceof ManagePermissionMessage) {
                                handlePermissionRequest((ManagePermissionMessage) message, user, clientChannel, currentChannel);
                                continue; // Don't broadcast
                            }

                            String content = message.getContent();

                            if (content != null && content.startsWith("/create-channel ")) {
                                String channelName = content.substring("/create-channel ".length()).trim();
                                handleCreateChannel(channelName, user, clientChannel);
                                continue;
                            }

                            if (content != null && content.startsWith("/join ")) {
                                String channelName = content.substring("/join ".length()).trim();
                                handleJoinChannel(channelName, user, clientChannel);
                                continue;
                            }

                            // Check if user has permission to send messages in current channel
                            if (!permissionManager.userHasPermission(user, currentChannel, Permission.WRITE)) {
                                sendSystemMessage(clientChannel, "Error: You don't have permission to send messages in #" + currentChannel);
                                continue;
                            }

                            broadcastMessageToChannel(message, usernameToChannel.get(username), currentChannel);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling client " + clientAddress + ": " + e.getMessage());
        } finally {
            disconnectClient(clientChannel);
        }
    }

    /**
     * Handles client disconnection cleanup.
     * Removes client from both maps and closes the channel.
     *
     * @param clientChannel the channel to disconnect
     */
    private void disconnectClient(SocketChannel clientChannel) {
        User user = channelToUser.remove(clientChannel);

        if (user != null) {
            usernameToChannel.remove(user.getUsername());
            userCurrentChannel.remove(user.getUsername());
            System.out.println("User disconnected: " + user.getUsername());
        }

        try {
            if (clientChannel.isOpen()) {
                clientChannel.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing channel: " + e.getMessage());
        }
    }

    private void handlePermissionRequest(ManagePermissionMessage msg, User requester, SocketChannel senderChannel, String channelId) throws IOException {
        if (!permissionManager.userHasPermission(requester, channelId, Permission.EDIT_PERMISSIONS)) {
            sendSystemMessage(senderChannel, "Error: Unauthorized to manage permissions in #" + channelId);
            return;
        }

        String targetUsername = msg.getTargetUsername();
        SocketChannel targetChannel = usernameToChannel.get(targetUsername);
        User targetUser;

        if (targetChannel != null) {
            targetUser = channelToUser.get(targetChannel);
        } else {
            targetUser = new User(targetUsername);
        }

        try {
            Permission permToAdd = msg.getPermission();
            String targetChannelId = msg.getChannelId();
            // Check for duplicates
            if (!permissionManager.userHasPermission(targetUser, targetChannelId, permToAdd)) {
                permissionManager.addPermission(targetUser, targetChannelId, permToAdd);
                sendSystemMessage(senderChannel, "Success: Added " + permToAdd + " to " + targetUsername + " in #" + targetChannelId);

                if (targetChannel != null) {
                    sendSystemMessage(targetChannel, "You have been granted permission: " + permToAdd + " in #" + targetChannelId);
                }
            } else {
                sendSystemMessage(senderChannel, "User already has permission: " + permToAdd + " in #" + targetChannelId);
            }
        } catch (IllegalArgumentException e) {
            sendSystemMessage(senderChannel, "Error: Invalid permission type.");
        }
    }

    private void handleJoinChannel(String channelName, User user, SocketChannel clientChannel) throws IOException {
        // Check if channel exists
        boolean channelExists = false;
        for (common.Channel channel : channels) {
            if (channel.getId().equals(channelName)) {
                channelExists = true;
                break;
            }
        }

        if (!channelExists) {
            sendSystemMessage(clientChannel, "Error: Channel #" + channelName + " does not exist.");
            return;
        }

        // Update user's current channel
        userCurrentChannel.put(user.getUsername(), channelName);
        sendSystemMessage(clientChannel, "Joined channel: #" + channelName);
        System.out.println(user.getUsername() + " joined channel: " + channelName);
    }

    private void handleCreateChannel(String channelName, User creator, SocketChannel clientChannel) throws IOException {
        if (channelName == null || channelName.isEmpty()) {
            sendSystemMessage(clientChannel, "Error: Channel name cannot be empty.");
            return;
        }

        // Check if channel already exists
        for (common.Channel existing : channels) {
            if (existing.getId().equals(channelName)) {
                return;
            }
        }

        common.Channel newChannel = new common.Channel(channelName);
        addChannel(newChannel);

        // Grant the creator full permissions in the new channel
        permissionManager.addPermission(creator, channelName, Permission.EDIT_PERMISSIONS);
        permissionManager.addPermission(creator, channelName, Permission.WRITE);
        permissionManager.addPermission(creator, channelName, Permission.JOIN);
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

            switch (simpleClassName) {
                case "TextMessage":
                    return TextMessage.deserialize(serializedData);
                case "AttachmentMessage":
                    return AttachmentMessage.deserialize(serializedData);
                case "ManagePermissionMessage":
                    return ManagePermissionMessage.deserialize(serializedData);
                default:
                    System.err.println("Unknown message type: " + className);
                    return null;
            }
        } catch (Exception e) {
            System.err.println("Error deserializing message: " + e.getMessage());
            return null;
        }
    }

    /**
     * Broadcasts a message to all users in a specific channel.
     * Sends a message to all connected clients in the channel except the sender
     *
     * @param message       the message to broadcast
     * @param senderChannel the channel of the sender (to exclude from broadcast)
     * @param channelId     the channel to broadcast to
     */
    private void broadcastMessageToChannel(Message message, SocketChannel senderChannel, String channelId) {
        for (Map.Entry<SocketChannel, User> entry : channelToUser.entrySet()) {
            SocketChannel channel = entry.getKey();
            User user = entry.getValue();

            // Don't send message back to sender
            if (channel.equals(senderChannel)) {
                continue;
            }

            // Only send to users in the same channel
            String userChannel = userCurrentChannel.get(user.getUsername());
            if (userChannel == null || !userChannel.equals(channelId)) {
                continue;
            }

            try {
                sendToClient(channel, message);
            } catch (IOException e) {
                System.err.println("Error sending to " + user.getUsername() + ": " + e.getMessage());
                // Schedule disconnection (can't modify map during iteration)
                clientHandlerPool.execute(() -> disconnectClient(channel));
            }
        }
    }

    /**
     * Broadcasts a message to all connected users (for system announcements).
     *
     * @param message       the message to broadcast
     * @param senderChannel the channel of the sender (to exclude from broadcast)
     */
    private void broadcastMessage(Message message, SocketChannel senderChannel) {
        for (Map.Entry<SocketChannel, User> entry : channelToUser.entrySet()) {
            SocketChannel channel = entry.getKey();
            User user = entry.getValue();

            // Don't send message back to sender
            if (channel.equals(senderChannel)) {
                continue;
            }

            try {
                sendToClient(channel, message);
            } catch (IOException e) {
                System.err.println("Error sending to " + user.getUsername() + ": " + e.getMessage());
                // Schedule disconnection (can't modify map during iteration)
                clientHandlerPool.execute(() -> disconnectClient(channel));
            }
        }
    }

    /** Sends a message from the system.
     *  Used for system administrative purposes
     *
     * @param client client to send the message to
     * @param content message to send
     * @throws IOException potentially if something fails with the sockets
     */
    public void sendSystemMessage(SocketChannel client, String content) throws IOException {
        Message message = new TextMessage("SYSTEM", content);
        sendToClient(client, message);
    }

    /**
     * Sends a message to a specific client.
     * Uses the protocol format described below
     *
     * @param clientChannel the channel to send to
     * @param message       the message to send
     * @throws IOException if an I/O error occurs
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
     * Adds a channel to channel list.
     *
     * @param channel channel to be added
     */
    public synchronized void addChannel(common.Channel channel) {
        // Avoid duplicate channels by id
        for (common.Channel existing : channels) {
            if (existing.getId().equals(channel.getId())) {
                System.out.println("Channel already exists: " + channel.getId());
                return;
            }
        }
        channels.add(channel);
        System.out.println("Channel added: " + channel.getId());

        // Notify all connected clients that a new channel was created
        TextMessage systemMessage = new TextMessage(
                "SYSTEM",
                "New channel created: #" + channel.getId(),
                LocalDateTime.now()
        );
        // senderChannel = null → goes to everyone (no one is excluded)
        broadcastMessage(systemMessage, null);
    }
}