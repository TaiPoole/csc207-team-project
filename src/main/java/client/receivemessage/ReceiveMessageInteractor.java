package client.receivemessage;

import common.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiveMessageInteractor implements ReceiveMessageInputBoundary {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private ReceiveMessageOutputBoundary presenter;

    public ReceiveMessageInteractor(ReceiveMessageOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(ReceiveMessageInputData inputData) {
        Message message = inputData.getMessage();

        if (message == null) {
            return;
        }

        String formattedTime = message.getTimestamp().format(TIME_FORMATTER);

        ReceiveMessageOutputData outputData = new ReceiveMessageOutputData(
                message.getUsername(),
                message.getContent(),
                formattedTime,
                message.getAttachment()
        );

        presenter.displayMessage(outputData);
    }
}
