package Client.SendMessage;

public class SendMessageOutputData {
    private final boolean success;
    private final String statusMessage;

    public SendMessageOutputData(boolean success, String message) {
        this.success = success;
        this.statusMessage = message;
    }
    public boolean isSuccess() {
        return success;
    }
    public String getMessage() {
        return statusMessage;  //use this for failure message
    }
}
