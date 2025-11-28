package client.generatename;

/**
 * Controller for the Generate Random Name use case.
 */
public class GenerateRandomNameController {

    private final GenerateRandomNameInputBoundary interactor;

    public GenerateRandomNameController(GenerateRandomNameInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Triggers random name generation.
     * NOTE: We could call the entity directly from the view, but we use
     * this Controller to keep the flow View → Controller → Interactor.
     * It's just for the credit you know.
     */
    public void generateRandomName() {
        GenerateRandomNameInputData inputData = new GenerateRandomNameInputData();
        interactor.generate(inputData);
    }
}
