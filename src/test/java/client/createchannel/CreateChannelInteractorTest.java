package client.createchannel;

import client.Client;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.function.Consumer;
import common.Message;
import static org.junit.jupiter.api.Assertions.*;

public class CreateChannelInteractorTest {

    @Test
    public void testNullChannelNameShowsErrorAndDoesNotCallClient() {
        TestClient client = new TestClient("testUser");

        TestPresenter presenter = new TestPresenter();
        CreateChannelInteractor interactor = new CreateChannelInteractor(client, presenter);
        interactor.execute(new CreateChannelInputData(null));
        assertNull(client.lastCommandSent,
                "Client should not be called when name is invalid");
        assertNull(presenter.lastSuccessData,
                "Success view must not be used for invalid input");
        assertEquals("Please enter a channel name.", presenter.lastErrorMessage);
    }

    @Test
    public void testValidChannelNameSendsCommandAndCallsSuccess() {
        TestClient client = new TestClient("testUser");

        TestPresenter presenter = new TestPresenter();
        CreateChannelInteractor interactor = new CreateChannelInteractor(client, presenter);
        interactor.execute(new CreateChannelInputData("  channel1  "));
        assertEquals("/create-channel channel1", client.lastCommandSent);
        assertNull(presenter.lastErrorMessage,
                "No error message on success");
        assertNotNull(presenter.lastSuccessData,
                "Success data should be passed to presenter");
        assertEquals("channel1", presenter.lastSuccessData.getChannelName());
    }

    @Test
    public void testIOExceptionShowsNetworkError() {
        TestClient client = new TestClient("testUser");
        client.throwIOException = true;

        TestPresenter presenter = new TestPresenter();
        CreateChannelInteractor interactor = new CreateChannelInteractor(client, presenter);
        interactor.execute(new CreateChannelInputData("network-room"));
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastErrorMessage);
        assertTrue(presenter.lastErrorMessage.startsWith("Failed to contact server:"));
    }

    @Test
    public void testUnexpectedExceptionShowsGenericError() {
        TestClient client = new TestClient("testUser");
        client.throwRuntimeException = true;

        TestPresenter presenter = new TestPresenter();
        CreateChannelInteractor interactor = new CreateChannelInteractor(client, presenter);
        interactor.execute(new CreateChannelInputData("boom-room"));
        assertNull(presenter.lastSuccessData);
        assertNotNull(presenter.lastErrorMessage);
        assertTrue(presenter.lastErrorMessage.startsWith("Unexpected error:"));
    }

    static class TestClient extends Client {

        String lastCommandSent = null;
        boolean throwIOException = false;
        boolean throwRuntimeException = false;

        TestClient(String username) {
            super(username, "localhost", new Consumer<Message>() {
                @Override
                public void accept(Message message) {
                }
            });
        }

        @Override
        public void sendMessage(String message) throws IOException {
            if (throwIOException) {
                throw new IOException("Simulated IO problem");
            }
            if (throwRuntimeException) {
                throw new RuntimeException("Simulated runtime problem");
            }
            lastCommandSent = message;
        }
    }

    static class TestPresenter implements CreateChannelOutputBoundary {

        CreateChannelOutputData lastSuccessData = null;
        String lastErrorMessage = null;

        @Override
        public void prepareSuccessView(CreateChannelOutputData data) {
            lastSuccessData = data;
        }
        @Override
        public void prepareFailureView(String errorMessage) {
            lastErrorMessage = errorMessage;
        }
    }
}
