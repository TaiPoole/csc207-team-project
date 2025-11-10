package Common;

import java.util.ArrayList;
import java.util.List;

import Client.Client;

public class Channel {
    final String id;
    List<Client> clients;
    List<Message> messages;

    public Channel(String id) {
        this.id = id;
        clients = new ArrayList<>();
        messages = new ArrayList<>();
    }
}
