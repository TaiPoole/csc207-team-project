package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagePermissionMessage implements Message {
    private final String username;
    private final String targetUsername;
    private final Permission permission;
    private final LocalDateTime timestamp;
    private final String channelId;

    public ManagePermissionMessage(String username, String targetUsername, Permission permission, String channelId, LocalDateTime timestamp) {
        this.username = username;
        this.targetUsername = targetUsername;
        this.permission = permission;
        this.timestamp = timestamp;
        this.channelId = channelId;
    }

    @Override
    public String serialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return getUsername() + "|" + targetUsername + "|" + permission.name() + "|" + channelId + "|" + getTimestamp().format(formatter);
    }

    public static ManagePermissionMessage deserialize(String data) {
        String[] parts = data.split("\\|");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid ManagePermissionMessage format");
        }

        String senderUsername = parts[0];
        String targetUsername = parts[1];
        Permission permission = Permission.valueOf(parts[2]);
        String channelId = parts[3];
        LocalDateTime timestamp = LocalDateTime.parse(parts[4], DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return new ManagePermissionMessage(senderUsername, targetUsername, permission, channelId, timestamp);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getContent() {
        return "Permission change: " + permission.name() + " for " + targetUsername;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Attachment getAttachment() {
        return null;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getChannelId() {
        return channelId;
    }
}