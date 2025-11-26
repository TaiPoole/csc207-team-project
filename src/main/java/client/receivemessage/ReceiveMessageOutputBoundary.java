package client.receivemessage;

/** ReceiveMessageOutputBoundary interface.
 *  requires the displaying of a received message
 */
public interface ReceiveMessageOutputBoundary {
    /** Adds a message to the UI.
     *
     * @param outputData message to be displayed
     */
    void  displayMessage(ReceiveMessageOutputData outputData);
}
