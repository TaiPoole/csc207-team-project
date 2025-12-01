package server;

import common.Permission;
import common.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Permission Manager.
 * Holds information regarding permissions for users in specific channels.
 * Each user can have different permissions in different channels.
 */
public class PermissionManager {
    private final Map<User, Map<String, ArrayList<Permission>>> userChannelPermissions;

    /**
     * Basic Constructor.
     * Initializes empty hashmap for per-channel permissions
     */
    public PermissionManager() {
        userChannelPermissions = new ConcurrentHashMap<>();
    }

    /**
     * Gets a given user's permissions in a specific channel.
     *
     * @param user      user to be checked
     * @param channelId the channel to check permissions in
     * @return list of permissions that user has in the specified channel
     */
    public ArrayList<Permission> getPermissions(User user, String channelId) {
        Map<String, ArrayList<Permission>> channelPerms = userChannelPermissions.get(user);
        if (channelPerms == null) {
            return new ArrayList<>();
        }

        ArrayList<Permission> perms = channelPerms.get(channelId);
        return perms != null ? perms : new ArrayList<>();
    }

    /**
     * Gets all channels where a user has any permissions.
     *
     * @param user user to be checked
     * @return map of channel IDs to permission lists
     */
    public Map<String, ArrayList<Permission>> getAllChannelPermissions(User user) {
        Map<String, ArrayList<Permission>> channelPerms = userChannelPermissions.get(user);
        return channelPerms != null ? new HashMap<>(channelPerms) : new HashMap<>();
    }

    /**
     * Adds permission to user's perms in a specific channel.
     *
     * @param user       recipient of perm
     * @param channelId  the channel where permission is granted
     * @param permission permission to be granted
     */
    public void addPermission(User user, String channelId, Permission permission) {
        userChannelPermissions
                .computeIfAbsent(user, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(channelId, k -> new ArrayList<>())
                .add(permission);
    }

    /**
     * Adds multiple permissions to user's perms in a specific channel.
     *
     * @param user        recipient of perms
     * @param channelId   the channel where permissions are granted
     * @param permissions list of permissions to be granted
     */
    public void addPermissions(User user, String channelId, ArrayList<Permission> permissions) {
        for (Permission permission : permissions) {
            addPermission(user, channelId, permission);
        }
    }

    /**
     * Manually sets permissions for a user in a specific channel.
     * This replaces any existing permissions in that channel.
     *
     * @param user        recipient of perm change
     * @param channelId   the channel to set permissions in
     * @param permissions list of permissions to grant
     */
    public void setPermissions(User user, String channelId, ArrayList<Permission> permissions) {
        userChannelPermissions
                .computeIfAbsent(user, k -> new ConcurrentHashMap<>())
                .put(channelId, new ArrayList<>(permissions));
    }

    /**
     * Removes a specific permission from a user in a channel.
     *
     * @param user       user to remove permission from
     * @param channelId  the channel to remove permission in
     * @param permission permission to remove
     * @return true if permission was removed, false if user didn't have it
     */
    public boolean removePermission(User user, String channelId, Permission permission) {
        Map<String, ArrayList<Permission>> channelPerms = userChannelPermissions.get(user);
        if (channelPerms == null) {
            return false;
        }

        ArrayList<Permission> perms = channelPerms.get(channelId);
        if (perms == null) {
            return false;
        }

        return perms.remove(permission);
    }

    /**
     * Removes all permissions for a user in a specific channel.
     *
     * @param user      user to clear permissions for
     * @param channelId the channel to clear permissions in
     */
    public void clearChannelPermissions(User user, String channelId) {
        Map<String, ArrayList<Permission>> channelPerms = userChannelPermissions.get(user);
        if (channelPerms != null) {
            channelPerms.remove(channelId);
        }
    }

    /**
     * Removes all permissions for a user across all channels.
     *
     * @param user user to clear all permissions for
     */
    public void clearAllPermissions(User user) {
        userChannelPermissions.remove(user);
    }

    /**
     * Checks if a user has a given permission in a specific channel.
     *
     * @param user       user of interest
     * @param channelId  the channel to check permission in
     * @param permission permission of interest
     * @return true if user has perm in the channel, false otherwise
     */
    public boolean userHasPermission(User user, String channelId, Permission permission) {
        Map<String, ArrayList<Permission>> channelPerms = userChannelPermissions.get(user);
        if (channelPerms == null) {
            return false;
        }

        ArrayList<Permission> perms = channelPerms.get(channelId);
        return perms != null && perms.contains(permission);
    }

    /**
     * Checks if a user has ANY permissions in a specific channel.
     *
     * @param user      user of interest
     * @param channelId the channel to check
     * @return true if user has at least one permission in the channel
     */
    public boolean userHasAnyPermission(User user, String channelId) {
        Map<String, ArrayList<Permission>> channelPerms = userChannelPermissions.get(user);
        if (channelPerms == null) {
            return false;
        }

        ArrayList<Permission> perms = channelPerms.get(channelId);
        return perms != null && !perms.isEmpty();
    }

    /**
     * Checks if a user has all specified permissions in a channel.
     *
     * @param user                user of interest
     * @param channelId           the channel to check
     * @param requiredPermissions permissions that must all be present
     * @return true if user has all required permissions
     */
    public boolean userHasAllPermissions(User user, String channelId, ArrayList<Permission> requiredPermissions) {
        for (Permission perm : requiredPermissions) {
            if (!userHasPermission(user, channelId, perm)) {
                return false;
            }
        }
        return true;
    }
}