package client.generatename;

import common.RandomNameGenerator;

/**
 * Use case interactor for generating a random username.
 */
public class GenerateRandomNameInteractor implements GenerateRandomNameInputBoundary {

    private final RandomNameGenerator randomNameGenerator;
    private final GenerateRandomNameOutputBoundary presenter;

    /**
     * Creates an interactor using a RandomNameGenerator entity and a presenter.
     *
     * @param randomNameGenerator the entity that produces random usernames
     * @param presenter the output boundary to receive the result
     */
    public GenerateRandomNameInteractor(RandomNameGenerator randomNameGenerator,
                                        GenerateRandomNameOutputBoundary presenter) {
        this.randomNameGenerator = randomNameGenerator;
        this.presenter = presenter;
    }

    /**
     * Executes the use case. Generates a random username and sends it to the presenter.
     *
     * @param inputData the unused input data object
     */
    @Override
    public void generate(GenerateRandomNameInputData inputData) {
        String name = randomNameGenerator.generate();
        GenerateRandomNameOutputData outputData = new GenerateRandomNameOutputData(name);
        presenter.present(outputData);
    }
}
