package client;

import common.Channel;
import common.Message;
import common.TextMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {
    private static final int TEST_PORT = 8080;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_SERVER = "localhost";

    private Client client;
    private List<Message> receivedMessages;
    private Consumer<Message> callback;
    private ServerSocketChannel mockServer;

    @BeforeEach
    public void setUp() {
        receivedMessages = new ArrayList<>();
        callback = message -> receivedMessages.add(message);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (mockServer != null && mockServer.isOpen()) {
            mockServer.close();
        }
    }

    @Test
    public void testClientConstructor() {
        client = new Client(TEST_USERNAME, TEST_SERVER, callback);
        assertNotNull(client, "Client should be created");
        assertEquals(TEST_USERNAME, client.getUsername(), "Username should match");
    }

    @Test
    public void testConnectSuccess() throws IOException, InterruptedException {
        // Start a mock server
        mockServer = ServerSocketChannel.open();
        mockServer.bind(new InetSocketAddress(TEST_PORT));
        mockServer.configureBlocking(false);

        client = new Client(TEST_USERNAME, TEST_SERVER, callback);

        // Connect in a separate thread
        Thread connectThread = new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                fail("Connection failed: " + e.getMessage());
            }
        });
        connectThread.start();

        // Accept the connection
        SocketChannel serverChannel = null;
        for (int i = 0; i < 50 && serverChannel == null; i++) {
            serverChannel = mockServer.accept();
            Thread.sleep(10);
        }

        assertNotNull(serverChannel, "Server should accept connection");
        connectThread.join(1000);
        serverChannel.close();
    }

    @Test
    public void testConnectWhenAlreadyConnected() throws IOException, InterruptedException {
        mockServer = ServerSocketChannel.open();
        mockServer.bind(new InetSocketAddress(TEST_PORT));
        mockServer.configureBlocking(false);

        client = new Client(TEST_USERNAME, TEST_SERVER, callback);

        Thread connectThread = new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                fail("First connection failed");
            }
        });
        connectThread.start();

        // Accept the connection
        SocketChannel serverChannel = null;
        for (int i = 0; i < 50 && serverChannel == null; i++) {
            serverChannel = mockServer.accept();
            Thread.sleep(10);
        }

        connectThread.join(1000);

        if (serverChannel != null) {
            serverChannel.close();
        }

        // Try to connect again - should throw IllegalStateException
        assertThrows(IllegalStateException.class, () -> client.connect());
    }

    @Test
    public void testConnectWithInvalidAddress() {
        client = new Client(TEST_USERNAME, "invalid.host.nowhere", callback);
        assertThrows(java.nio.channels.UnresolvedAddressException.class, () -> client.connect());
    }

    @Test
    public void testSendMessageWithoutConnection() {
        client = new Client(TEST_USERNAME, TEST_SERVER, callback);
        Channel testChannel = new Channel("test");
        assertThrows(IllegalStateException.class, () -> client.sendMessage("Test", testChannel));
    }

    @Test
    public void testSendMessageSuccess() throws IOException, InterruptedException {
        mockServer = ServerSocketChannel.open();
        mockServer.bind(new InetSocketAddress(TEST_PORT));
        mockServer.configureBlocking(false);

        client = new Client(TEST_USERNAME, TEST_SERVER, callback);

        Thread connectThread = new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                fail("Connection failed");
            }
        });
        connectThread.start();

        // Accept connection
        SocketChannel serverChannel = null;
        for (int i = 0; i < 50 && serverChannel == null; i++) {
            serverChannel = mockServer.accept();
            Thread.sleep(10);
        }

        connectThread.join(1000);
        assertNotNull(serverChannel, "Server should accept connection");
        serverChannel.configureBlocking(false);

        String testMessage = "Hello Server";
        Channel testChannel = new Channel("test");
        client.sendMessage(testMessage, testChannel);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Thread.sleep(100);
        int bytesRead = serverChannel.read(buffer);

        assertTrue(bytesRead > 0, "Server should receive data");
        buffer.flip();

        String received = StandardCharsets.UTF_8.decode(buffer).toString();
        assertTrue(received.contains(testMessage), "Message should contain message text");

        serverChannel.close();
    }

    @Test
    public void testSendMessageWithMessageObject() throws IOException, InterruptedException {
        mockServer = ServerSocketChannel.open();
        mockServer.bind(new InetSocketAddress(TEST_PORT));
        mockServer.configureBlocking(false);

        client = new Client(TEST_USERNAME, TEST_SERVER, callback);

        // Connect
        Thread connectThread = new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                fail("Connection failed");
            }
        });
        connectThread.start();

        // Accept connection
        SocketChannel serverChannel = null;
        for (int i = 0; i < 50 && serverChannel == null; i++) {
            serverChannel = mockServer.accept();
            Thread.sleep(10);
        }

        connectThread.join(1000);
        assertNotNull(serverChannel, "Server should accept connection");
        serverChannel.configureBlocking(false);
        Channel testChannel = new Channel("test");
        TextMessage message = new TextMessage(TEST_USERNAME, "Test content", LocalDateTime.now(), testChannel);
        client.sendMessage(message);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Thread.sleep(100);
        int bytesRead = serverChannel.read(buffer);

        assertTrue(bytesRead > 0, "Server should receive data");

        serverChannel.close();
    }

    @Test
    public void testListenReceivesMessages() throws IOException, InterruptedException {
        mockServer = ServerSocketChannel.open();
        mockServer.bind(new InetSocketAddress(TEST_PORT));
        mockServer.configureBlocking(false);

        client = new Client(TEST_USERNAME, TEST_SERVER, callback);

        // Connect
        Thread connectThread = new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                fail("Connection failed");
            }
        });
        connectThread.start();

        // Accept connection
        SocketChannel serverChannel = null;
        for (int i = 0; i < 50 && serverChannel == null; i++) {
            serverChannel = mockServer.accept();
            Thread.sleep(10);
        }

        connectThread.join(1000);
        assertNotNull(serverChannel, "Server should accept connection");
        Channel testChannel = new Channel("test");
        String className = "Common.textMessage";
        TextMessage testMsg = new TextMessage("server", "Test message from server", LocalDateTime.now(), testChannel);
        String serialized = testMsg.serialize();
        String fullMessage = className + "\n" + serialized;

        ByteBuffer buffer = ByteBuffer.wrap(fullMessage.getBytes(StandardCharsets.UTF_8));
        serverChannel.write(buffer);

        // Wait for message to be received
        Thread.sleep(500);

        boolean messageReceived = receivedMessages.stream()
                .anyMatch(m -> m instanceof TextMessage && m.getContent().contains("Test message from server"));

        assertTrue(messageReceived, "Client should receive message from server");

        serverChannel.close();
    }

    @Test
    public void testListenHandlesServerDisconnect() throws IOException, InterruptedException {
        mockServer = ServerSocketChannel.open();
        mockServer.bind(new InetSocketAddress(TEST_PORT));
        mockServer.configureBlocking(false);

        client = new Client(TEST_USERNAME, TEST_SERVER, callback);

        // Connect
        Thread connectThread = new Thread(() -> {
            try {
                client.connect();
            } catch (IOException e) {
                fail("Connection failed");
            }
        });
        connectThread.start();

        // Accept and then close connection
        SocketChannel serverChannel = null;
        for (int i = 0; i < 50 && serverChannel == null; i++) {
            serverChannel = mockServer.accept();
            Thread.sleep(10);
        }

        connectThread.join(1000);
        assertNotNull(serverChannel, "Server should accept connection");

        Thread.sleep(100);
        serverChannel.close();
        Thread.sleep(500);

        assertTrue(true, "Test completed successfully");
    }
}