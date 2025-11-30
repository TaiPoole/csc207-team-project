package client.searchmessage;

import interfaceadapter.ChatViewModel;
import java.util.ArrayList;
import java.util.List;

public class SearchMessageInteractor implements SearchMessageInputBoundary {

    private final SearchMessageOutputBoundary presenter;
    private final ChatViewModel chatViewModel;

    public SearchMessageInteractor(ChatViewModel chatViewModel,
                                   SearchMessageOutputBoundary presenter) {
        this.chatViewModel = chatViewModel;
        this.presenter = presenter;
    }

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