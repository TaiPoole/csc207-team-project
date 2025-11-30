package client.searchmessage;

/**
 * Output boundary for the Search Message use case.
 */
public interface SearchMessageOutputBoundary {

    /**
     * Presents the search results.
     *
     * @param outputData the output data
     */
    void present(SearchMessageOutputData outputData);
}
