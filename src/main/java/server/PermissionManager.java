package server;

import common.User;
import common.Permission;

import java.util.HashMap;
import java.util.ArrayList;

/** Permission Manager.
 *  Holds information regarding permissions for users in a given channel
 */
public class PermissionManager {
    HashMap<User, ArrayList<Permission>> permissions;

    /** Basic Constructor.
     *  Initializes empty hashmap
     */
    public PermissionManager() {
        permissions = new HashMap<>();
    }

    /** Gets a given users permissions.
     *
     * @param user user to be checked
     * @return list of permissions that user has (in the user's channel)
     */
    public ArrayList<Permission> getPermissions(User user) {
        return permissions.get(user);
    }

    /** Adds permission to users perms.
     *
     * @param user recipient of perm
     * @param permission permissions to be granted
     */
    public void addPermission(User user, Permission permission) {
        permissions.computeIfAbsent(user, k -> new ArrayList<>()).add(permission);
    }

    /** Manually sets permissions for a user.
     *
     * @param user recipient of perm change
     * @param permissions list of permissions to grant
     */
    public void setPermissions(User user, ArrayList<Permission> permissions) {
        this.permissions.put(user, permissions);
    }

    /** Checks if a user has a given permission.
     *
     * @param user user of interest
     * @param permission permission of interest
     * @return True if user has perm, False otherwise
     */
    public boolean userHasPermission(User user, Permission permission) {
        return permissions.containsKey(user) && permissions.get(user).contains(permission);
    }
}
