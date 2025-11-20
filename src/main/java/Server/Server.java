package Server;

import Common.Message;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ArrayList<Common.Channel> channels; // server channels, not used for communication
    private final Map<String, User> connectedUsers;

    private final int port;
    private ServerSocketChannel serverChannel;
    private ExecutorService clientHandlerPool;
    private Thread acceptThread;


    public Server(int port) {
        this.channels = new ArrayList<>();
        this.port = port;
        this.connectedUsers = new HashMap<>();
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

        acceptThread = new Thread(this::acceptClients);
        acceptThread.start();
    }

    private void acceptClients() {
        while (true) {
            try {
                SocketChannel clientChannel = serverChannel.accept();
                String clientId = clientChannel.getRemoteAddress().toString();

                // Add to connected clients map
                User clientUser = new User(clientId, clientChannel);
                connectedUsers.put(clientId, clientUser);

                clientHandlerPool.execute(() -> handleClient(clientChannel, clientId));

            } catch (IOException e) {
                // TODO
            }
        }
    }

    private void handleClient(SocketChannel clientChannel, String clientId) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            while (clientChannel.isOpen()) {
                buffer.clear();
                int bytesRead = clientChannel.read(buffer);

                if (bytesRead == -1) {
                    break;
                }

                if (bytesRead > 0) {
                    buffer.flip();
                    Message message = new Message(StandardCharsets.UTF_8.decode(buffer).toString());
                    sendToClient(clientChannel, message);
                }
            }
        } catch (IOException e) {
            // TODO: error handling here
        }
    }

    public void sendToClient(SocketChannel clientChannel, Message message) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(message.serialize().getBytes(StandardCharsets.UTF_8));
        clientChannel.write(buffer);
    }

    // TODO: implement abstaction with Message for this function
    public void sendToAll(String message) throws IOException {
        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            String clientId = entry.getKey();
            User user = entry.getValue();
            SocketChannel channel = user.getChannel();

            if (channel.isOpen()) {
                //sendToClient(channel, message);
            } else {
                connectedUsers.remove(clientId);
            }
        }
    }

    public void addChannel(Common.Channel channel){
        channels.add(channel);
    }
}
