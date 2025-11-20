package GUI;
import Common.Channel;
import Server.Server;
public class AddChannelButton extends Button {
    public AddChannelButton(){
        super();
    }

    public void createNewChannel(String name, Server server){
        Channel c = new Channel(name);
        server.addChannel(c);
    }

}
