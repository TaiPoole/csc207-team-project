package client.receivemessage;

import common.Message;

public class ReceiveMessageInputData {
    Message message;

    public ReceiveMessageInputData(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }
}
