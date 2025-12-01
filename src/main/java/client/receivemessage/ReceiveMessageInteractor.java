package client.receivemessage;

import common.Message;
import java.time.format.DateTimeFormatter;

/** ReceiveMessage Interactor class.
 *  follows ReceiveMessageInputBoundary restrictions
 *  in charge of managing incoming received messages and turning them to an output format its presenter can use
 */
public class ReceiveMessageInteractor implements ReceiveMessageInputBoundary {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ReceiveMessageOutputBoundary presenter;

    /** Basic Constructor.
     *
     * @param presenter presenter for ui displaying once the input is formatted
     */
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
