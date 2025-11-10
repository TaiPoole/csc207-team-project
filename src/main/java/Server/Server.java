package Server;

import Common.User;

import java.util.ArrayList;
import java.util.List;
import java.nio.channels.Channel;

public class Server {
    private List<Channel> client_channels;
    private List<Common.Channel> channels;
    private List<User> users;

    public Server() {
        client_channels = new ArrayList<>();
        channels = new ArrayList<>();
        users = new ArrayList<>();
    }
}
