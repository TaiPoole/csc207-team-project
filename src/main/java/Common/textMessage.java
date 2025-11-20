package Common;

import java.time.LocalDateTime;

public class textMessage implements MessageInterface {
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public textMessage(String username, String content, LocalDateTime timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String serialize() {
        return this.username + "\n" + this.content + "\n" + this.timestamp;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }



}
