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

    // TODO: Checkstyle error with this not being used. I think this might be impossible to refactor with our current logic. First and only suppress? -Tai
    /**
     * Reconstructs a message from its serialized string format.
     * Implementations should override this method.
     *
     * @return the deserialized Message object, or null if invalid
     */
    static Message deserialize() {
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

    /** Gets a potential attachment.
     *
     * @return attachment for the message (always exists in the contexts it's called)
     */
    Attachment getAttachment();

    /** Get the channel that the message is in.
     *
     * @return the channel
     */
    Channel getChannel();
}
