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

    public void addPermission(User user, Permission permission) {
        this.permissions.get(user).add(permission);
    }

    public void setPermissions(User user, ArrayList<Permission> permissions) {
        this.permissions.put(user, permissions);
    }

    public boolean userHasPermission(User user, Permission permission) {
        return permissions.get(user).contains(permission);
    }
}
