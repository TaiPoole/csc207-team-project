package common;

import java.util.Random;

public class RandomNameGenerator {

    private static final String[] ADJECTIVES = {
            "Quiet", "Happy", "Sleepy", "Cosmic", "Shiny", "Brave", "Silly"
    };

    private static final String[] ANIMALS = {
            "Panda", "Fox", "Otter", "Tiger", "Penguin", "Cat", "Dog"
    };

    private final Random random = new Random();

    public String generate() {
        String adj = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String animal = ANIMALS[random.nextInt(ANIMALS.length)];
        int number = random.nextInt(1000);
        return adj + animal + "#" + number;
    }
}