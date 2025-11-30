package client.searchmessage;

import java.util.List;

/**
 * Output data for the Search Message use case.
 */
public class SearchMessageOutputData {

    private final List<String> matchingMessages;

    /**
     * Constructs output data with the matching messages.
     *
     * @param matchingMessages messages that match the search
     */
    public SearchMessageOutputData(List<String> matchingMessages) {
        this.matchingMessages = matchingMessages;
    }

    /**
     * Returns the matching messages.
     *
     * @return the list of messages
     */
    public List<String> getMatchingMessages() {
        return matchingMessages;
    }
}
