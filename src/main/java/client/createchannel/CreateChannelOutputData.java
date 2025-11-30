package client.createchannel;

/**
 * Data returned by the Create Channel use case on success.
 */
public class CreateChannelOutputData {

    private final String channelName;

    public CreateChannelOutputData(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
