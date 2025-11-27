package permissions;

public class ManagePermissionsInputData {
    final String username;
    final String permissionName;

    public ManagePermissionsInputData(String username, String permissionName) {
        this.username = username;
        this.permissionName = permissionName;
    }

    public String getUsername() {
        return username;
    }

    public String getPermissionName() {
        return permissionName;
    }
}