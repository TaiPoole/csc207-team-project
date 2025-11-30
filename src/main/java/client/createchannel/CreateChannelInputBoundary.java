package client.createchannel;

/**
 * Input boundary for the Create Channel use case.
 */
public interface CreateChannelInputBoundary {
    void execute(CreateChannelInputData inputData);
}
