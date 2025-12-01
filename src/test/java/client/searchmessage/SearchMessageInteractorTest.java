package client.searchmessage;

import interfaceadapter.ChatViewModel;
import org.junit.jupiter.api.Test;
import javax.swing.DefaultListModel;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class SearchMessageInteractorTest {
    @Test
    public void testNullQueryReturnsEmptyResults() {
        DefaultListModel<String> messageModel = new DefaultListModel<>();
        ChatViewModel chatViewModel = new ChatViewModel(messageModel);
        TestPresenter presenter = new TestPresenter();
        SearchMessageInteractor interactor = new SearchMessageInteractor(chatViewModel, presenter);
        interactor.execute(new SearchMessageInputData(null));

        assertNotNull(presenter.lastOutputData,
                "Presenter should still be called");
        assertTrue(presenter.lastOutputData.getMatchingMessages().isEmpty(),
                "Null query should give empty result list");
    }

    @Test
    public void testSearchFindsMatchesOnlyInActiveChannel() {
        DefaultListModel<String> messageModel = new DefaultListModel<>();
        ChatViewModel chatViewModel = new ChatViewModel(messageModel);
        TestPresenter presenter = new TestPresenter();

        chatViewModel.addMessage("general", "Hello");
        chatViewModel.addMessage("general", "Hello Everynyan");
        chatViewModel.addMessage("other", "OH MY GAH!");
        chatViewModel.addMessage("other", "Hello");

        chatViewModel.setActiveChannel("general");

        SearchMessageInteractor interactor = new SearchMessageInteractor(chatViewModel, presenter);

        interactor.execute(new SearchMessageInputData("hello"));

        assertNotNull(presenter.lastOutputData);
        List<String> results = presenter.lastOutputData.getMatchingMessages();

        assertEquals(2, results.size());
        assertFalse(results.get(0).contains("Hello Everynyan"));
    }

    static class TestPresenter implements SearchMessageOutputBoundary {

        SearchMessageOutputData lastOutputData = null;

        @Override
        public void present(SearchMessageOutputData outputData) {
            this.lastOutputData = outputData;
        }
    }
}
