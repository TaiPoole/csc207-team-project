package client.createchannel;

/**
 * Controller for the Create Channel use case.
 * Called by the view (AddChannelDialog).
 */
public class CreateChannelController {

    private final CreateChannelInputBoundary interactor;

    /**
     * Constructs a controller for the create channel use case.
     *
     * @param interactor the input boundary
     */
    public CreateChannelController(CreateChannelInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Sends the channel name to the interactor.
     *
     * @param rawName the user-input channel name
     */
    public void createChannel(String rawName) {
        CreateChannelInputData inputData =
                new CreateChannelInputData(rawName);
        interactor.execute(inputData);
    }
}
