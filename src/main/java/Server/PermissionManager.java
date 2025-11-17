package Server;

import Common.Permission;

import java.util.ArrayList;
import java.util.HashMap;

public class PermissionManager {
    HashMap<User, ArrayList<Permission>> permissions;

    public PermissionManager() {
        permissions = new HashMap<User, ArrayList<Permission>>();
    }

    public ArrayList<Permission> getPermissions(User user) {
        return permissions.get(user);
    }

    public boolean userHasPermission(User user, Permission permission) {
        return permissions.get(user).contains(permission);
    }
}
