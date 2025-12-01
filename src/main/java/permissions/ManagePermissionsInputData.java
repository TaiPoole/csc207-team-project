package permissions;

/** Input class for managing permissions.
 *  holds the data needed to try to change a perm
 *
 */
public class ManagePermissionsInputData {
    final String username;
    final String permissionName;
    final String currentUser;
    final String channel;

    /** Basic constructor.
     *
     * @param currentUser current user that attempts the change
     * @param username user to be changed
     * @param permissionName perm to be changed
     * @param channel channel to change perm in
     */
    public ManagePermissionsInputData(String currentUser, String username, String permissionName, String channel) {
        this.currentUser = currentUser;
        this.username = username;
        this.permissionName = permissionName;
        this.channel = channel;
    }

    public String getUsername() {
        return username;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}