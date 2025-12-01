package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagePermissionMessage implements Message {
    private final String username;
    private final String targetUsername;
    private final Permission permission;
    private final LocalDateTime timestamp;

    public ManagePermissionMessage(String username, String targetUsername,
                                   Permission permission, LocalDateTime timestamp) {
        this.username = username;
        this.targetUsername = targetUsername;
        this.permission = permission;
        this.timestamp = timestamp;
    }

    @Override
    public String serialize() {
        return username + "|" + targetUsername + "|" + permission.name() + "|"
                + timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static ManagePermissionMessage deserialize(String data) {
        String[] parts = data.split("\\|");
        return new ManagePermissionMessage(
                parts[0],
                parts[1],
                Permission.valueOf(parts[2]),
                LocalDateTime.parse(parts[3], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
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
}