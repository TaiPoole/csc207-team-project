package Client.SendMessage;
import Client.Client;
import Common.Message;

import java.io.IOException;
import java.time.LocalDateTime;


public class SendMessageInteractor implements SendMessageInputBoundary {
    private final SendMessageOutputBoundary presenter;
    private final Client client;

    public SendMessageInteractor(SendMessageOutputBoundary presenter, Client client ) {

        this.presenter = presenter;
        this.client = client;

    }

    public void execute(SendMessageInputData input) {
        if (input.getMessageContent() == null || input.getMessageContent().trim().isEmpty()) {
            presenter.prepareFailureView("Message cannot be empty");
            return;
        }

        try {
            Message message;
            if (input.hasAttachment()) {
                message = new Message(client.getUsername(), input.getMessageContent(), LocalDateTime.now(), input.getAttachment());
            }
            else {
                message = new Message(client.getUsername(), input.getMessageContent(), LocalDateTime.now());
            }

            client.sendMessage(message);
            presenter.prepareSuccessView();
        }
        catch (IOException e) {
            presenter.prepareFailureView("Failure to send message" + e.getMessage());
        }
        catch (Exception e) {
            presenter.prepareFailureView("Error" + e.getMessage());
        }
    }


}

