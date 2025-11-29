package client.generatename;

import common.RandomNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the GenerateRandomNameInteractor use case.
 */
public class GenerateRandomNameInteractorTest {

    /**
     * A simple test of RandomNameGenerator.
     * It always returns the same fixed name and counts how many times generate() is called.
     */
    static class TestRandomNameGenerator extends RandomNameGenerator {

        int generateCallCount = 0;
        String fixedName;

        TestRandomNameGenerator(String fixedName) {
            this.fixedName = fixedName;
        }

        @Override
        public String generate() {
            generateCallCount = generateCallCount + 1;
            return fixedName;
        }
    }

    /**
     * A simple test of the presenter.
     * It remembers the last name it received and how many times present() was called.
     */
    static class TestPresenter implements GenerateRandomNameOutputBoundary {

        int presentCallCount = 0;
        String lastPresentedName = null;

        @Override
        public void present(GenerateRandomNameOutputData outputData) {
            presentCallCount = presentCallCount + 1;
            if (outputData != null) {
                lastPresentedName = outputData.getGeneratedName();
            }
        }
    }

    @Test
    public void testGenerateCallsGeneratorAndPresenterOnce() {
        String expectedName = "TestName#123";
        TestRandomNameGenerator testGenerator = new TestRandomNameGenerator(expectedName);
        TestPresenter testPresenter = new TestPresenter();

        GenerateRandomNameInteractor interactor =
                new GenerateRandomNameInteractor(testGenerator, testPresenter);

        GenerateRandomNameInputData inputData = new GenerateRandomNameInputData();

        interactor.generate(inputData);

        assertEquals(1, testGenerator.generateCallCount,
                "generate() should call RandomNameGenerator.generate exactly once");
        assertEquals(1, testPresenter.presentCallCount,
                "generate() should call presenter.present exactly once");

        assertNotNull(testPresenter.lastPresentedName,
                "Presenter should receive a name");
        assertEquals(expectedName, testPresenter.lastPresentedName,
                "Presenter should receive the same name that the generator created");
    }

    @Test
    public void testGenerateCanBeCalledMultipleTimes() {
        TestRandomNameGenerator testGenerator = new TestRandomNameGenerator("AnyName#999");
        TestPresenter testPresenter = new TestPresenter();

        GenerateRandomNameInteractor interactor =
                new GenerateRandomNameInteractor(testGenerator, testPresenter);

        GenerateRandomNameInputData inputData = new GenerateRandomNameInputData();

        interactor.generate(inputData);
        interactor.generate(inputData);

        assertEquals(2, testGenerator.generateCallCount,
                "Generator should be called once for each generate(...) call");
        assertEquals(2, testPresenter.presentCallCount,
                "Presenter should be called once for each generate(...) call");
        assertEquals("AnyName#999", testPresenter.lastPresentedName,
                "The last presented name should come from the generator");
    }
}