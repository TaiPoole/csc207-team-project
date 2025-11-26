package client.sendmessage;

/** Output message interface.
 *  this interface requires handling of the message failing and succeeding to be sent out
 */
public interface SendMessageOutputBoundary {
    /** What happens when thw message is sent properly.
     *
     * @param outputData information that was successfully sent out
     */
    void prepareSuccessView(SendMessageOutputData outputData);

    /** What happens when the message fails to send.
     *
     * @param errorMessage error message to be displayed
     */
    void prepareFailureView(String errorMessage);
}
