package common;

import java.util.Random;

/** RandomNameGenerator class.
 *  Manages the generation of random usernames if the user decides to change name
 */
public class RandomNameGenerator {

    private static final String[] ADJECTIVES = {
            "Quiet", "Happy", "Sleepy", "Cosmic", "Shiny", "Brave", "Silly"
    };

    private static final String[] ANIMALS = {
            "Panda", "Fox", "Otter", "Tiger", "Penguin", "Cat", "Dog"
    };

    private final Random random = new Random();

    /** Generates new random username.
     *  new name is just a random adjective, animal and user number
     *
     * @return new username
     */
    public String generate() {
        String adj = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String animal = ANIMALS[random.nextInt(ANIMALS.length)];
        int number = random.nextInt(1000);
        return adj + animal + "#" + number;
    }
}