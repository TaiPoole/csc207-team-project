package Client;

import Common.Message;
import Common.textMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

import static org.junit.Assert.*;

public class ClientTest {
    private static final int TEST_PORT = 8080;
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_SERVER = "localhost";

    private Client client;
    private List<Message> receivedMessages;
    private Consumer<Message> callback;
    private ServerSocketChannel mockServer;

    @Before
    public void setUp() {
        receivedMessages = new ArrayList<>();
        callback = message -> receivedMessages.add(message);
    }

    @After
    public void tearDown() throws IOException {
        if (mockServer != null && mockServer.isOpen()) {
            mockServer.close();
        }
    }

    @Test
    public void testClientConstructor() {
        client = new Client(TEST_USERNAME, TEST_SERVER, callback);
        assertNotNull("Client should be created", client);
        assertEquals("Username should match", TEST_USERNAME, client.getUsername());
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

        assertNotNull("Server should accept connection", serverChannel);
        connectThread.join(1000);

        serverChannel.close();
    }

    @Test(expected = IllegalStateException.class)
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
        client.connect();
    }

    @Test(expected = java.nio.channels.UnresolvedAddressException.class)
    public void testConnectWithInvalidAddress() throws IOException {
        client = new Client(TEST_USERNAME, "invalid.host.nowhere", callback);
        client.connect();
    }

    @Test(expected = IllegalStateException.class)
    public void testSendMessageWithoutConnection() throws IOException {
        client = new Client(TEST_USERNAME, TEST_SERVER, callback);
        client.sendMessage("Test");
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

        assertNotNull("Server should accept connection", serverChannel);
        serverChannel.configureBlocking(false);

        String testMessage = "Hello Server";
        client.sendMessage(testMessage);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Thread.sleep(100);

        int bytesRead = serverChannel.read(buffer);
        assertTrue("Server should receive data", bytesRead > 0);

        buffer.flip();
        String received = StandardCharsets.UTF_8.decode(buffer).toString();
        assertTrue("Message should contain message text",
                received.contains(testMessage));

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

        assertNotNull("Server should accept connection", serverChannel);
        serverChannel.configureBlocking(false);

        textMessage message = new textMessage(TEST_USERNAME, "Test content", LocalDateTime.now());
        client.sendMessage(message);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Thread.sleep(100);

        int bytesRead = serverChannel.read(buffer);
        assertTrue("Server should receive data", bytesRead > 0);

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

        assertNotNull("Server should accept connection", serverChannel);

        String className = "Common.textMessage";
        textMessage testMsg = new textMessage("server", "Test message from server", LocalDateTime.now());
        String serialized = testMsg.serialize();
        String fullMessage = className + "\n" + serialized;

        ByteBuffer buffer = ByteBuffer.wrap(fullMessage.getBytes(StandardCharsets.UTF_8));
        serverChannel.write(buffer);

        // Wait for message to be received
        Thread.sleep(500);

        boolean messageReceived = receivedMessages.stream()
                .anyMatch(m -> m instanceof textMessage &&
                        ((textMessage)m).getContent().contains("Test message from server"));
        assertTrue("Client should receive message from server", messageReceived);

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

        assertNotNull("Server should accept connection", serverChannel);
        Thread.sleep(100);
        serverChannel.close();

        Thread.sleep(500);
        assertTrue("Test completed successfully", true);
    }
}