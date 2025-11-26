package common;

import java.time.LocalDateTime;

/** TextMessage Class.
 *  a type of Message
 *  holds information about sender, content, and the time it was sent
 */
public class TextMessage implements Message {
    private final String username;
    private final String content;
    private final LocalDateTime timestamp;

    /** Basic Constructor.
     *
     * @param username user that sent it
     * @param content contents of the image
     * @param timestamp when it was sent
     */
    public TextMessage(String username, String content, LocalDateTime timestamp) {
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    /** Deserializes message.
     *  message format just holds our 3 TextMessage fields seperated by newlines
     *
     * @param message message to be deserialized
     * @return TextMessage constructed from message contents
     * @throws IllegalArgumentException if message is malformed
     */
    public static Message deserialize(String message) throws IllegalArgumentException {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("Serialized message cannot be null or empty");
        }

        String[] parts = message.split("\n", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid format");
        }

        return new TextMessage(parts[0], parts[2], LocalDateTime.parse(parts[1]));
    }

    /** Serializes message.
     *  follows format specified in deserialize
     *
     * @return TextMessage-converted String
     */
    public String serialize() {
        return this.username + "\n" + this.timestamp + "\n" + this.content;
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

    public Attachment getAttachment() {
        return null;
    }

}
