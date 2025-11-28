package client.generatename;

import interfaceadapter.RandomNameViewModel;

/**
 * Presenter for the Generate Random Name use case.
 */
public class GenerateRandomNamePresenter implements GenerateRandomNameOutputBoundary {

    private final RandomNameViewModel viewModel;

    /**
     * Creates a presenter connected to a RandomNameViewModel.
     *
     * @param viewModel the ViewModel to update with the new name
     */
    public GenerateRandomNamePresenter(RandomNameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Updates the ViewModel with the newly generated name.
     *
     * @param outputData contains the generated username
     */
    @Override
    public void present(GenerateRandomNameOutputData outputData) {
        viewModel.setLatestName(outputData.getGeneratedName());
    }
}
