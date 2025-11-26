package client.sendmessage;

import client.Client;
import common.AttachmentMessage;
import common.Message;
import common.TextMessage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Send Message Interactor class.
 *  inherits restrictions of SendMessageInputBoundary
 *  handles the logic for sending a message.
 */
public class SendMessageInteractor implements SendMessageInputBoundary {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final SendMessageOutputBoundary presenter;
    private final Client client;

    /** Basic Constructor.
     *
     * @param presenter ui manager for updating
     * @param client client to send message to
     */
    public SendMessageInteractor(SendMessageOutputBoundary presenter, Client client) {
        this.presenter = presenter;
        this.client = client;
    }

    /** Handles potential message with sending.
     *
     * @param input data for a potential outbound message
     */
    public void execute(SendMessageInputData input) {
        if (input.getMessageContent() == null || input.getMessageContent().trim().isEmpty()) {
            presenter.prepareFailureView("Message cannot be empty");
            return;
        }

        try {
            Message message;
            if (input.hasAttachment()) {
                message = new AttachmentMessage(client.getUsername(), input.getMessageContent(), LocalDateTime.now(), input.getAttachment());
            } else {
                message = new TextMessage(client.getUsername(), input.getMessageContent(), LocalDateTime.now());
            }

            client.sendMessage(message);

            String formattedTime = LocalDateTime.now().format(TIME_FORMATTER);
            SendMessageOutputData outputData = new SendMessageOutputData(
                    client.getUsername(),
                    input.getMessageContent(),
                    formattedTime
            );

            presenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            presenter.prepareFailureView("Failure to send message" + e.getMessage());
        } catch (Exception e) {
            presenter.prepareFailureView("Error" + e.getMessage());
        }
    }
}

