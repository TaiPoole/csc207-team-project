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
    @Override
    public void execute(SendMessageInputData input) {
        String content = input.getMessageContent();
        if (content == null) {
            content = "";
        }
        String trimmedContent = content.trim();

        // Disallow completely empty messages
        if (trimmedContent.isEmpty() && !input.hasAttachment()) {
            presenter.prepareFailureView("Message cannot be empty.");
            return;
        }

        String channelId = client.getCurrentChannel();
        if (channelId == null || channelId.isEmpty()) {
            channelId = "general";
        }

        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(TIME_FORMATTER);

        String wireContent;
        if (trimmedContent.isEmpty()) {
            wireContent = "[" + channelId + "]";
        } else {
            wireContent = "[" + channelId + "] " + trimmedContent;
        }

        try {
            Message messageToSend;

            if (input.hasAttachment()) {
                messageToSend = new AttachmentMessage(
                        client.getUsername(),
                        wireContent,
                        now,
                        input.getAttachment()
                );
            } else {
                messageToSend = new TextMessage(
                        client.getUsername(),
                        wireContent,
                        now
                );
            }

            // Send using existing API
            client.sendMessage(messageToSend);

            // For the local UI, we show only the "clean" text, without [channel]
            SendMessageOutputData outputData = new SendMessageOutputData(
                    client.getUsername(),
                    trimmedContent,                 // <- no [channel] prefix in UI
                    formattedTime,
                    input.getAttachment()
            );

            presenter.prepareSuccessView(outputData);
        } catch (IOException e) {
            presenter.prepareFailureView("Failure to send message" + e.getMessage());
        } catch (Exception e) {
            presenter.prepareFailureView("Error" + e.getMessage());
        }
    }

}

