package Common;

import java.time.LocalDateTime;

interface MessageInterface {

    String serialize();

    String getUsername();

    String getContent();

    LocalDateTime getTimestamp();


}
