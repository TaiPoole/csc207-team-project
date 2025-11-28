package client.generatename;

/**
 * Input boundary for the Generate Random Name use case.
 */
public interface GenerateRandomNameInputBoundary {
    /**
     * Executes the use case of generating a new random username.
     *
     *  @param inputData empty one, actually. (kept for CA consistency)
     */
    void generate(GenerateRandomNameInputData inputData);
}
