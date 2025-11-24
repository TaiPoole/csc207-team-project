package common;

/**
 * A file Attachment to a message.
 */
public final class Attachment {
    private final String name;
    private final byte[] file;

    /**
     * Create an Attachment.
     */
    public Attachment(String name, byte[] file) {
        this.name = name;
        this.file = file;
    }

    public byte[] getAttachment() {
        return file;
    }

    public String getName() {
        return name;
    }
}
