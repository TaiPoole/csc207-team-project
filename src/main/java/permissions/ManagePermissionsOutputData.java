package permissions;

public class ManagePermissionsOutputData {
    final String message;
    final boolean success;

    public ManagePermissionsOutputData(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}