package Client.SendMessage;

public interface SendMessageOutputBoundary {
    void prepareSuccessView(SendMessageOutputData outputData);
    void prepareFailureView(String errorMessage);
}
