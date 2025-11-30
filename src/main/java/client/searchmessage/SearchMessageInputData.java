package client.searchmessage;

/**
 * Input data for the Search Message use case.
 */
public class SearchMessageInputData {

    private final String term;

    /**
     * Constructs input data with the given search term.
     *
     * @param term the term to search for
     */
    public SearchMessageInputData(String term) {
        this.term = term;
    }

    /**
     * Returns the search term.
     *
     * @return the term
     */
    public String getTerm() {
        return term;
    }
}
