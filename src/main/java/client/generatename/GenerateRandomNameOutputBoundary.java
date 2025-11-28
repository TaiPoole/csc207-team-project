package client.generatename;

/**
 * Output boundary for the Generate Random Name use case.
 */
public interface GenerateRandomNameOutputBoundary {
    /**
     * Presents the result of the use case to the UI layer.
     *
     * @param outputData contains the newly generated username
     */
    void present(GenerateRandomNameOutputData outputData);
}