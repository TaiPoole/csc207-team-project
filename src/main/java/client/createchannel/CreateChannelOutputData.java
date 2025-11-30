package client.createchannel;

/**
 * Data returned by the Create Channel use case on success.
 */
public class CreateChannelOutputData {

    private final String channelName;

    /**
     * Constructs the output data.
     *
     * @param channelName the created channel name
     */
    public CreateChannelOutputData(String channelName) {
        this.channelName = channelName;
    }

    /**
     * Returns the channel name.
     *
     * @return the name
     */
    public String getChannelName() {
        return channelName;
    }
}
