package common;

/** User class.
 *  For the server to organize senders/recipients within a given channel.
 */
public class User {
    private final String username;

    /**
     * Create User
     */
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
