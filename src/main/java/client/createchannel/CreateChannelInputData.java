package client.createchannel;

/**
 * Data passed into the Create Channel use case.
 */
public class CreateChannelInputData {

    private final String channelName;

    /**
     * Constructs input data for creating a channel.
     *
     * @param channelName the name of the channel
     */
    public CreateChannelInputData(String channelName) {
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
