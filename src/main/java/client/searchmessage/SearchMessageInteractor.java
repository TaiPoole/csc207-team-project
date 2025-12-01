package client.searchmessage;

import interfaceadapter.ChatViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Interactor for the Search Message use case.
 */
public class SearchMessageInteractor implements SearchMessageInputBoundary {

    private final SearchMessageOutputBoundary presenter;
    private final ChatViewModel chatViewModel;

    /**
     * Constructs the interactor.
     *
     * @param chatViewModel the chat view model holding messages
     * @param presenter the output boundary
     */
    public SearchMessageInteractor(ChatViewModel chatViewModel,
                                   SearchMessageOutputBoundary presenter) {
        this.chatViewModel = chatViewModel;
        this.presenter = presenter;
    }

    /**
     * Executes a search on the current channel.
     *
     * @param inputData the search term input
     */
    @Override
    public void execute(SearchMessageInputData inputData) {
        String query = inputData.getTerm();
        if (query == null || query.isEmpty()) {
            presenter.present(new SearchMessageOutputData(new ArrayList<>()));
            return;
        }

        String lower = query.toLowerCase();
        List<String> inChannel = chatViewModel.getMessagesForActiveChannel();
        List<String> results = new ArrayList<>();

        for (String msg : inChannel) {
            if (msg.toLowerCase().contains(lower)) {
                results.add(msg);
            }
        }

        presenter.present(new SearchMessageOutputData(results));
    }
}