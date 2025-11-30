package client.receivemessage;

import common.Attachment;
import common.AttachmentMessage;
import common.Message;
import common.TextMessage;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ReceiveMessageInteractorTest {

    @Test
    public void testReceiveTextMessage() throws InterruptedException {
        DefaultListModel<String> messageModel = new DefaultListModel<>();
        ReceiveMessagePresenter presenter = new ReceiveMessagePresenter(messageModel);
        ReceiveMessageInteractor interactor = new ReceiveMessageInteractor(presenter);

        LocalDateTime timestamp = LocalDateTime.of(2025, 11, 29, 13, 59, 25);

        Message message = new TextMessage("test-user", "test-message", timestamp);
        ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        String displayedMessage = messageModel.getElementAt(0);
        assertTrue(displayedMessage.contains("test-user"));
        assertTrue(displayedMessage.contains("test-message"));
        assertTrue(displayedMessage.contains("13:59:25"));
    }

    @Test
    public void testReceiveAttachmentMessage() throws InterruptedException {
        DefaultListModel<String> messageModel = new DefaultListModel<>();
        ReceiveMessagePresenter presenter = new ReceiveMessagePresenter(messageModel);
        ReceiveMessageInteractor interactor = new ReceiveMessageInteractor(presenter);

        byte[] fileData = "test-file".getBytes();
        Attachment attachment = new Attachment("file.pdf", fileData);
        LocalDateTime timestamp = LocalDateTime.of(2025, 11, 29, 13, 59, 25);
        Message message = new AttachmentMessage("test-user", "test-message", timestamp, attachment);

        ReceiveMessageInputData inputData = new ReceiveMessageInputData(message);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(1, messageModel.getSize());
        String displayedMessage = messageModel.getElementAt(0);
        assertTrue(displayedMessage.contains("test-user"));
        assertTrue(displayedMessage.contains("test-message"));
        assertTrue(displayedMessage.contains("13:59:25"));
        assertTrue(displayedMessage.contains("file.pdf"));
    }

    @Test
    public void testReceiveNullMessage() throws InterruptedException {
        DefaultListModel<String> messageModel = new DefaultListModel<>();
        ReceiveMessagePresenter presenter = new ReceiveMessagePresenter(messageModel);
        ReceiveMessageInteractor interactor = new ReceiveMessageInteractor(presenter);

        ReceiveMessageInputData inputData = new ReceiveMessageInputData(null);

        interactor.execute(inputData);
        Thread.sleep(100);

        assertEquals(0, messageModel.getSize());
    }
}

