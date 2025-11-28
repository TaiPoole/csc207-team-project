package client.generatename;

/**
 * Output data for the Generate Random Name use case.
 */
public class GenerateRandomNameOutputData {

    private final String generatedName;

    public GenerateRandomNameOutputData(String generatedName) {
        this.generatedName = generatedName;
    }

    public String getGeneratedName() {
        return generatedName;
    }
}
