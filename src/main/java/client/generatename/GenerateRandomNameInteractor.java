package client.generatename;

import common.RandomNameGenerator;

/**
 * Use case interactor for generating a random username.
 */
public class GenerateRandomNameInteractor implements GenerateRandomNameInputBoundary {

    private final RandomNameGenerator randomNameGenerator;
    private final GenerateRandomNameOutputBoundary presenter;

    public GenerateRandomNameInteractor(RandomNameGenerator randomNameGenerator,
                                        GenerateRandomNameOutputBoundary presenter) {
        this.randomNameGenerator = randomNameGenerator;
        this.presenter = presenter;
    }

    // acutally not need to do this, keep just for structure.
    @Override
    public void generate(GenerateRandomNameInputData inputData) {
        String name = randomNameGenerator.generate();
        GenerateRandomNameOutputData outputData = new GenerateRandomNameOutputData(name);
        presenter.present(outputData);
    }
}
