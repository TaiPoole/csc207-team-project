package client.searchmessage;

import interfaceadapter.SearchMessageViewModel;

public class SearchMessagePresenter implements SearchMessageOutputBoundary {

    private final SearchMessageViewModel viewModel;

    public SearchMessagePresenter(SearchMessageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SearchMessageOutputData searchMessageOutputData) {
        viewModel.setResults(searchMessageOutputData.getMatchingMessages());
    }
}
