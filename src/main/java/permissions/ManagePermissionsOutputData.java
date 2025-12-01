package permissions;

/** Class to describe outputs for managing permissions.
 *  Mostly for wrapping the output so you can get extra context, since this is an op that can fail
 */
public class ManagePermissionsOutputData {
    final String message;
    final boolean success;

    /** Basic constructor.
     *
     * @param message message crafted
     * @param success indicates success of changing the permissions
     */
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