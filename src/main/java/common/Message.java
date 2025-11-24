package common;

import java.time.LocalDateTime;

/**
 * Represents a message that can be transmitted between client and server.
 */
public interface Message {

    /**
     * Converts this message to a string format for transmission.
     *
     * @return serialized string representation of the message
     */
    String serialize();

    /**
     * Reconstructs a message from its serialized string format.
     * Implementations should override this method.
     *
     * @param message the serialized message string
     * @return the deserialized Message object, or null if invalid
     */
    static Message deserialize(String message) {
        return null;
    }

    /**
     * Gets the username of the message sender.
     *
     * @return the sender's username
     */
    String getUsername();

    /**
     * Gets the text content of the message.
     *
     * @return the message content
     */
    String getContent();

    /**
     * Gets the timestamp when the message was created.
     *
     * @return the message timestamp
     */
    LocalDateTime getTimestamp();

    Attachment getAttachment();
}
