package client.createchannel;

/**
 * Output boundary for the Create Channel use case.
 */
public interface CreateChannelOutputBoundary {

    /**
     * Presents the success view.
     *
     * @param data output data for the created channel
     */
    void prepareSuccessView(CreateChannelOutputData data);

    /**
     * Presents the failure view.
     *
     * @param errorMessage the error message
     */
    void prepareFailureView(String errorMessage);
}
