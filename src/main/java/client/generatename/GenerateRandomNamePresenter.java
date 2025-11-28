package client.generatename;

import interface_adapter.RandomNameViewModel;

/**
 * Presenter for the Generate Random Name use case.
 */
public class GenerateRandomNamePresenter implements GenerateRandomNameOutputBoundary {

    private final RandomNameViewModel viewModel;

    public GenerateRandomNamePresenter(RandomNameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GenerateRandomNameOutputData outputData) {
        String name = outputData.getGeneratedName();

            viewModel.setLatestName(name);
    }
}
