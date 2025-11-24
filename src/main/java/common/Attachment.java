package common;

import java.awt.image.BufferedImage;

public final class Attachment {
    private final BufferedImage image;

    public Attachment(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }
}
