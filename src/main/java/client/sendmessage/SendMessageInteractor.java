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
        boolean hasText = content != null && !content.trim().isEmpty();
        boolean hasAttachment = input.hasAttachment();

        // Don't allow completely empty messages (no text, no file)
        if (!hasText && !hasAttachment) {
            presenter.prepareFailureView("Message cannot be empty");
            return;
        }

        try {
            // 1. Figure out which channel we're in
            String channelId = client.getCurrentChannel();
            if (channelId == null || channelId.isEmpty()) {
                channelId = "general";
            }

            // 2. Prepare safe text versions
            String safeContent = (content == null) ? "" : content.trim();

            // Wire content is what goes over the network, with [channel] prefix
            // We want format "[channel] message" or just "[channel]" if no text
            String wireContent;
            if (safeContent.isEmpty()) {
                wireContent = "[" + channelId + "]";
            } else {
                wireContent = "[" + channelId + "] " + safeContent;
            }

            // 3. Build the actual Message (with channel-tagged content)
            LocalDateTime now = LocalDateTime.now();
            Message message;

            if (hasAttachment) {
                message = new AttachmentMessage(
                        client.getUsername(),
                        wireContent,
                        now,
                        input.getAttachment()
                );
            } else {
                message = new TextMessage(
                        client.getUsername(),
                        wireContent,
                        now
                );
            }

            // 4. Send using existing API
            client.sendMessage(message);

            // 5. For the local UI, we show only the "clean" text, without [channel]
            String formattedTime = now.format(TIME_FORMATTER);
            SendMessageOutputData outputData = new SendMessageOutputData(
                    client.getUsername(),
                    safeContent,                 // <- no [channel] prefix in UI
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

