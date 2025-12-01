package client.searchmessage;

/**
 * Controller for the Search Message use case.
 */
public class SearchMessageController {

    private final SearchMessageInputBoundary interactor;

    /**
     * Constructs a SearchMessageController.
     *
     * @param interactor the input boundary for the use case
     */
    public SearchMessageController(SearchMessageInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes a search request.
     *
     * @param term the search term
     */
    public void search(String term) {
        SearchMessageInputData inputData = new SearchMessageInputData(term);
        interactor.execute(inputData);
    }
}
