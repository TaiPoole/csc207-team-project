package common;

import java.time.LocalDateTime;
import java.util.ArrayList;

/** Channel Class.
 *  a bundle of users, holds info for server organization
 */
public class Channel {
    final String id;
    ArrayList<User> clients;
    LocalDateTime timestamp;

    /** Basic constructor.
     *  initializes with empty client and message lists
     *
     * @param id id to assign
     */
    public Channel(String id,  LocalDateTime timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    /**
     * Constructs a channel with the given ID.
     *
     * @param id the channel identifier
     */
    public Channel(String id) {
        this.id = id;
    }

    /** Returns the identifier/name of this channel. */
    public String getId() {
        return id;
    }

    /** Add users to a channel.
     *
     * @param users users to be added
     */
    public void inviteUsers(ArrayList<User> users) {
        for (User user : users) {
            if (!this.clients.contains(user)) {
                this.clients.add(user);
            }
        }
    }

}
