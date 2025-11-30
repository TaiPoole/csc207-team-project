package client.searchmessage;

/**
 * Input boundary for the Search Message use case.
 */
public interface SearchMessageInputBoundary {

    /**
     * Executes the search use case.
     *
     * @param inputData the search input
     */
    void execute(SearchMessageInputData inputData);
}
