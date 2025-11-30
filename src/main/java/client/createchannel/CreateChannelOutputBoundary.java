package client.createchannel;

/**
 * Output boundary for the Create Channel use case.
 */
public interface CreateChannelOutputBoundary {

    void prepareSuccessView(CreateChannelOutputData data);

    void prepareFailureView(String errorMessage);
}
