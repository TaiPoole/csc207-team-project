package server;

import common.Channel;
import java.nio.channels.SocketChannel;

/** User class.
 *  For the server to organize senders/recipients within a given channel.
 */
public class User {
    private final String username;
    private final SocketChannel socketChannel;
    private Channel currentChannel;

    /** Basic User constructor.
     *
     * @param username username
     * @param socketChannel channel that the user belongs to
     */
    public User(String username, SocketChannel socketChannel) {
        this.username = username;
        this.socketChannel = socketChannel;
    }

    public SocketChannel getChannel() {
        return socketChannel;
    }

    public String getUsername() {
        return username;
    }

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(Channel channel) {
        this.currentChannel = channel;
    }
}
