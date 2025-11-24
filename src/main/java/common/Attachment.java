package common;


public final class Attachment {
    private final String name;
    private final byte[] file;

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
