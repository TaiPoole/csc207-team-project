package Client;
import Common.Channel;
import Server.Server;
public class Client_Functions {
    /**
     * join channel, leave channel, private channel. link to the GUI buttons and stuff like that.**/
    public void createNewChannel(String name){
        Channel channel = new Channel(name);
    }
}
