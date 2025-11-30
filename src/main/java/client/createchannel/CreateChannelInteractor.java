package client.createchannel;

import client.Client;
import java.io.IOException;

/**
 * Interactor for creating a new channel.
 * Implements the business rules for the Create Channel use case.
 */
public class CreateChannelInteractor implements CreateChannelInputBoundary {

    private final Client client;
    private final CreateChannelOutputBoundary presenter;

    /**
     * Constructs the interactor.
     *
     * @param client the client used to send commands
     * @param presenter the output boundary
     */
    public CreateChannelInteractor(Client client,
                                   CreateChannelOutputBoundary presenter) {
        this.client = client;
        this.presenter = presenter;
    }

    /**
     * Executes the use case.
     *
     * @param inputData the channel name input
     */
    @Override
    public void execute(CreateChannelInputData inputData) {
        String rawName = inputData.getChannelName();

        if (rawName == null || rawName.trim().isEmpty()) {
            presenter.prepareFailureView("Please enter a channel name.");
            return;
        }

        String name = rawName.trim();

        try {
            // Protocol: send a special command message to the server.
            String command = "/create-channel " + name;
            client.sendMessage(command);

            CreateChannelOutputData outputData =
                    new CreateChannelOutputData(name);
            presenter.prepareSuccessView(outputData);

        } catch (IOException e) {
            presenter.prepareFailureView(
                    "Failed to contact server: " + e.getMessage()
            );
        } catch (Exception e) {
            presenter.prepareFailureView(
                    "Unexpected error: " + e.getMessage()
            );
        }
    }
}
