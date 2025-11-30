package client.createchannel;

/**
 * Data passed into the Create Channel use case.
 */
public class CreateChannelInputData {

    private final String channelName;

    public CreateChannelInputData(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelName() {
        return channelName;
    }
}
