package client.receivemessage;

/** ReceiveMessageInputBoundary interface.
 *  requires the ability to execute on raw input data
 */
public interface ReceiveMessageInputBoundary {

    /** Executes upon raw input data.
     *  Handles input for a potentially outbound message
     *
     * @param inputData raw input to be turned into receivable message format
     */
    void execute(ReceiveMessageInputData inputData, String username);
}
