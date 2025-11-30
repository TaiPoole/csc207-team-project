package client.createchannel;

/**
 * Input boundary for the Create Channel use case.
 */
public interface CreateChannelInputBoundary {

    /**
     * Executes the use case.
     *
     * @param inputData the channel name input
     */
    void execute(CreateChannelInputData inputData);
}
