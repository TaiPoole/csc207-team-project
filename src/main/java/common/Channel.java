package common;

import client.Client;
import java.util.ArrayList;
import java.util.List;

/** Channel Class.
 *  a bundle of users, holds info for server organization
 */
public class Channel {
    final String id;
    List<Client> clients;
    List<Message> messages;

    /** Basic constructor.
     *  initializes with empty client and message lists
     *
     * @param id id to assign
     */
    public Channel(String id) {
        this.id = id;
        clients = new ArrayList<>();
        messages = new ArrayList<>();
    }
}
