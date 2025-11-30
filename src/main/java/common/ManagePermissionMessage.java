package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagePermissionMessage implements Message {
    private final String username;
    private final String targetUsername;
    private final String permissionName;
    private final LocalDateTime timestamp;

    public ManagePermissionMessage(String username, String targetUsername,
                                   String permissionName, LocalDateTime timestamp) {
        this.username = username;
        this.targetUsername = targetUsername;
        this.permissionName = permissionName;
        this.timestamp = timestamp;
    }

    @Override
    public String serialize() {
        return username + "|" + targetUsername + "|" + permissionName + "|"
                + timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static ManagePermissionMessage deserialize(String data) {
        String[] parts = data.split("\\|");
        return new ManagePermissionMessage(
                parts[0],
                parts[1],
                parts[2],
                LocalDateTime.parse(parts[3], DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getContent() {
        return "Permission change: " + permissionName + " for " + targetUsername;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Attachment getAttachment() {
        return null;
    }
}