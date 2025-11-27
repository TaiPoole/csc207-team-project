package common;

import java.time.LocalDateTime;
import java.util.ArrayList;
import server.User;

/** Channel Class.
 *  a bundle of users, holds info for server organization
 */
public class Channel {
    final String id;
    ArrayList<User> clients;
    LocalDateTime timestamp;
    ArrayList<Message> messages;

    /** Basic constructor.
     *  initializes with empty client and message lists
     *
     * @param id id to assign
     */
    public Channel(String id,  LocalDateTime timestamp) {
        this.id = id;
        this.timestamp = timestamp;
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

    /** Add message to this.messages.
     *
     * @param message message to be added
     */
    public void addMessage(Message message) {
        this.messages.add(message);
    }

}
