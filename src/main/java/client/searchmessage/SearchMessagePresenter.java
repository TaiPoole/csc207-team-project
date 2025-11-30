package client.searchmessage;

import interfaceadapter.SearchMessageViewModel;

/**
 * Presenter for the Search Message use case.
 */
public class SearchMessagePresenter implements SearchMessageOutputBoundary {

    private final SearchMessageViewModel viewModel;

    /**
     * Constructs the presenter.
     *
     * @param viewModel the view model to update
     */
    public SearchMessagePresenter(SearchMessageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Updates the view model with the search results.
     *
     * @param outputData the output data
     */
    @Override
    public void present(SearchMessageOutputData outputData) {
        viewModel.setResults(outputData.getMatchingMessages());
    }
}
