package client.searchmessage;

import java.util.List;

public class SearchMessageOutputData {

    private final List<String> matchingMessages;

    public SearchMessageOutputData(List<String> matchingMessages) {
        this.matchingMessages = matchingMessages;
    }

    public List<String> getMatchingMessages() {
        return matchingMessages;
    }
}
