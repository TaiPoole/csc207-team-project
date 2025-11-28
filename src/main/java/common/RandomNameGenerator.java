package common;

import java.util.Random;

/** Entity responsible for generating random usernames.
 *  Manages the generation of random usernames if the user decides to change name
 */
public class RandomNameGenerator {

    private static final String[] DEFAULT_ADJECTIVES = {
            "Quiet", "Happy", "Sleepy", "Cosmic", "Shiny", "Brave", "Silly"
    };

    private static final String[] DEFAULT_ANIMALS = {
            "Panda", "Fox", "Otter", "Tiger", "Penguin", "Cat", "Dog"
    };

    private Random random = new Random();


    /** Generates new random username.
     *  new name is just a random adjective, animal and user number
     *
     * @return new username
     */
    public String generate() {
        String adj = DEFAULT_ADJECTIVES[random.nextInt(DEFAULT_ADJECTIVES.length)];
        String animal = DEFAULT_ANIMALS[random.nextInt(DEFAULT_ANIMALS.length)];
        int number = random.nextInt(1000);
        return adj + animal + "#" + number;
    }
}