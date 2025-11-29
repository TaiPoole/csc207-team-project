package client.sendmessage;

import client.Client;
import common.Attachment;
import common.Message;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.io.IOException;

public class SendMessageInteractorTest {

    @Test
    public void testSendMessage() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        TestClient client = new TestClient("testUser");

        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData("test-message");

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertNotNull(client.lastMessageSent);
        assertEquals("test-message", client.lastMessageSent.getContent());
    }

    @Test
    public void testSendMessageWithAttachment() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        TestClient client = new TestClient("testUser");

        byte[] testFileBytes = "test file content".getBytes();
        Attachment attachment = new Attachment("test.txt", testFileBytes);
        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData("test-message", attachment);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertNotNull(client.lastMessageSent);
        assertEquals("test-message", client.lastMessageSent.getContent());
    }

    @Test
    public void testSendNullMessageWithAttachment() throws InterruptedException{
        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        TestClient client = new TestClient("testUser");

        byte[] testFileBytes = "test file content".getBytes();
        Attachment attachment = new Attachment("test.txt", testFileBytes);
        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData(null, attachment);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertNotNull(client.lastMessageSent);
        assertEquals("", client.lastMessageSent.getContent());
    }

    @Test
    public void testSendEmptyMessage() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        Client client = new Client("testUser", "localhost", null);

        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData(" ", null);

        interactor.execute(inputData);
        Thread.sleep(100); // Wait for SwingUtilities.invokeLater

        assertEquals(1, messageModel.getSize());
        assertTrue(messageModel.getElementAt(0).contains("Message cannot be empty"));
    }

    @Test
    public void testSendNullMessage() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        Client client = new Client("testUser", "localhost", null);

        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData(null);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertTrue(messageModel.getElementAt(0).contains("Message cannot be empty"));
    }

    @Test
    public void testSendMessageNotConnected() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        Client client = new Client("testUser", "localhost", null);

        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData("test-message");

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertTrue(messageModel.getElementAt(0).contains("ERROR"));
    }

    @Test
    public void testSendMessageWithAttachmentNotConnected() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        Client client = new Client("testUser", "localhost", null);

        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        byte[] testFileBytes = "test file content".getBytes();
        Attachment attachment = new Attachment("testfile.txt", testFileBytes);
        SendMessageInputData inputData = new SendMessageInputData("Hello World", attachment);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertTrue(messageModel.getElementAt(0).contains("ERROR"));
    }

    @Test
    public void testGetUsernameReturnsCorrect() {

        Client client = new Client("myTestUser", "localhost", null);
        assertEquals("myTestUser", client.getUsername());
    }

    @Test
    public void testInputDataWithoutAttachmentHasNoAttachment() {

        SendMessageInputData inputData = new SendMessageInputData("Test message");

        assertFalse(inputData.hasAttachment());
        assertNull(inputData.getAttachment());
    }

    @Test
    public void testInputDataWithAttachmentHasAttachment() {

        byte[] testFileBytes = "test file content".getBytes();
        Attachment attachment = new Attachment("testfile.txt", testFileBytes);
        SendMessageInputData inputData = new SendMessageInputData("Test message", attachment);

        assertTrue(inputData.hasAttachment());
        assertNotNull(inputData.getAttachment());
        assertEquals(attachment, inputData.getAttachment());
    }


    @Test
    public void testIOException() throws InterruptedException {

        DefaultListModel<String> messageModel = new DefaultListModel<>();
        SendMessagePresenter presenter = new SendMessagePresenter(messageModel);
        TestClient client = new TestClient("testUser");
        client.shouldThrowIOException = true;

        SendMessageInteractor interactor = new SendMessageInteractor(presenter, client);
        SendMessageInputData inputData = new SendMessageInputData("Test message");

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        assertTrue(messageModel.getElementAt(0).contains("Failure to send message"));
    }

    static class TestClient extends Client {
        Message lastMessageSent = null;
        boolean shouldThrowIOException = false;

        public TestClient(String username) {
            super(username, "localhost", null);
        }

        @Override
        public void sendMessage(Message message) throws IOException{
            if (shouldThrowIOException) {
                throw new IOException("Network error");
            }
            lastMessageSent = message;
        }

    }
}