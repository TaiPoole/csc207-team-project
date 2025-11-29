package interfaceadapter;

import common.Attachment;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores attachments associated with messages in the message list.
 * Maps list indices to Attachment objects so the view can let the user download them.
 */
public class AttachmentRegistry {

    private final Map<Integer, Attachment> attachments = new HashMap<>();

    /**
     * Register an attachment for the given message index.
     * If attachment is null, any previous mapping for this index is removed.
     *
     * @param index      index of the message in the JList / ListModel
     * @param attachment attachment object; may be null
     */
    public void registerAttachment(int index, Attachment attachment) {
        if (attachment == null) {
            attachments.remove(index);
        } else {
            attachments.put(index, attachment);
        }
    }

    /**
     * Get the attachment associated with the given message index.
     *
     * @param index index of the message in the JList / ListModel
     * @return the Attachment, or null if none is registered
     */
    public Attachment getAttachment(int index) {
        return attachments.get(index);
    }

    /**
     * Clear any attachment associated with the given index.
     */
    public void clearAttachment(int index) {
        attachments.remove(index);
    }

    /**
     * Clear all registered attachments.
     * Call this if the entire message list is cleared.
     */
    public void clear() {
        attachments.clear();
    }
}
