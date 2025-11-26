package client.receivemessage;

import common.Message;

/** ReceiveMessageInputData class.
 *  handles the inputs for an incoming message
 */
public class ReceiveMessageInputData {
    Message message;

    /** Basic Constructor.
     *
     * @param message input message
     */
    public ReceiveMessageInputData(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
