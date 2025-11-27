package permissions;

import common.Permission;
import common.User;
import common.ManagePermissionMessage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class ServerPermissionsGateway {
    private final SocketChannel serverConnection;

    public ServerPermissionsGateway(SocketChannel serverConnection) {
        this.serverConnection = serverConnection;
    }

    public boolean requestPermissionChange(String currentUser, User user, Permission permission) {
        try {
            ManagePermissionMessage message = new ManagePermissionMessage(
                    currentUser,
                    user.getUsername(),
                    permission.name(),
                    LocalDateTime.now()
            );

            String serialized = message.getClass().getName() + "\n" + message.serialize();
            ByteBuffer buffer = ByteBuffer.wrap(serialized.getBytes(StandardCharsets.UTF_8));
            serverConnection.write(buffer);

            return true;
        } catch (IOException e) {
            System.err.println("Failed to send permission request: " + e.getMessage());
            return false;
        }
    }
}