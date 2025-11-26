package client.sendmessage;

/** SendMessageInputBoundary interface.
 *  requires the ability to execute on raw input data
 */
public interface SendMessageInputBoundary {

    /** Executes upon raw input data.
     *  Handles input for a potentially outbound message
     *
     * @param message raw input to be turned into sendable message formats
     */
    void execute(SendMessageInputData message);

}

