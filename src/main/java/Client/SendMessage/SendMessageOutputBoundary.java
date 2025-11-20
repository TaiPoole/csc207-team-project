package Client.SendMessage;

public interface SendMessageOutputBoundary {
    void prepareSuccessView();
    void prepareFailureView(String errorMessage);
}
