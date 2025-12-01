package permissions;

public class ManagePermissionsInputData {
    final String username;
    final String permissionName;
    final String currentUser;
    final String channel;

    public ManagePermissionsInputData(String currentUser, String username, String permissionName) {
        this.currentUser = currentUser;
        this.username = username;
        this.permissionName = permissionName;
        this.channel = "general";
    }

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