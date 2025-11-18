package InterfaceAdaptor.SendMessage;

import UseCase.SendMessage.SendMessageOutputBoundary;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class SendMessagePresenter implements SendMessageOutputBoundary {
    private final DefaultListModel<String> messageModel;

    public SendMessagePresenter(DefaultListModel<String> messageModel) {
            this.messageModel = messageModel;
    }

    @Override
    public void prepareSuccessView() {
        // Silent success
    }

    @Override
    public void prepareFailureView(String error) {
        SwingUtilities.invokeLater(() -> {
            messageModel.addElement("ERROR: " + error);
            });
    }
}
