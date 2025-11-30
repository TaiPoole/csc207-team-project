package client.createchannel;

/**
 * Controller for the Create Channel use case.
 * Called by the view (AddChannelDialog).
 */
public class CreateChannelController {

    private final CreateChannelInputBoundary interactor;

    public CreateChannelController(CreateChannelInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void createChannel(String rawName) {
        CreateChannelInputData inputData =
                new CreateChannelInputData(rawName);
        interactor.execute(inputData);
    }
}
