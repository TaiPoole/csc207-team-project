package Common;

import java.time.LocalDateTime;

public class Text_Message implements Message_Interface{
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public Text_Message(String username, String content, LocalDateTime timestamp) {
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
