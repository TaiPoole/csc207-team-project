package common;

import client.Client;

import java.util.ArrayList;
import java.util.List;

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
