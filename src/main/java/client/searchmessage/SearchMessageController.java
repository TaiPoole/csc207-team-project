package client.searchmessage;

public class SearchMessageController {

    private final SearchMessageInputBoundary interactor;

    public SearchMessageController(SearchMessageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void search(String term) {
        SearchMessageInputData inputData = new SearchMessageInputData(term);
        interactor.execute(inputData);
    }
}
