package UseCase.SendMessage;
import Client.Client;
import Common.Message;
import Common.User;

import java.io.IOException;
import java.time.LocalDateTime;


public class SendMessageInteractor {
    private final SendMessageOutputBoundary presenter;
    private final Client client;
    private final User  user;


    public SendMessageInteractor(SendMessageOutputBoundary presenter, Client client, User user) {

        this.presenter = presenter;
        this.client = client;
        this.user = user;

    }

    public void execute(SendMessageInputData input) {
        if (input.getMessageContent() == null || !input.getMessageContent().trim().isEmpty()) {
            presenter.prepareFailureView("Message cannot be empty");
            return;
        }

        try {
            Message message;
            if (input.hasAttachment()) {
                message = new Message(user, input.getMessageContent(), new LocalDateTime(), input.getAttachment());
            }
            else {
                message = new Message(user, input.getMessageContent(), new LocalDateTime());
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

