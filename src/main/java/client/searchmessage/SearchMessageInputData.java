package client.searchmessage;

public class SearchMessageInputData {

    private final String term;

    public SearchMessageInputData(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }
}
