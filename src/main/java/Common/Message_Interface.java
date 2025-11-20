package Common;

import java.time.LocalDateTime;

interface Message_Interface {

    String serialize();

    String getUsername();

    String getContent();

    LocalDateTime getTimestamp();


}
