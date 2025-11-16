package UseCase.SendMessage;

public interface SendMessageOutputBoundary {
    void prepareSuccessView(SendMessageOutputData message);
    void prepareFailureView(String errorMessage);
}
