package client.generatename;

/**
 * Output data for the Generate Random Name use case.
 */
public class GenerateRandomNameOutputData {

    private final String generatedName;

    /**
     * Creates a new output data object containing the generated username.
     *
     * @param generatedName the new random username
     */
    public GenerateRandomNameOutputData(String generatedName) {
        this.generatedName = generatedName;
    }

    /**
     * a getter.
     *
     * @return the newly generated username
     */
    public String getGeneratedName() {
        return generatedName;
    }
}
