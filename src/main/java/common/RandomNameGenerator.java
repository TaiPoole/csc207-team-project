package common;

import java.util.Random;

/** Entity responsible for generating random usernames.
 *  Manages the generation of random usernames if the user decides to change name
 */
public class RandomNameGenerator {

    private static final String[] DEFAULT_ADJECTIVES = {
            "Quiet", "Happy", "Sleepy", "Cosmic", "Shiny", "Brave", "Silly",
            "Gentle", "Lucky", "Rapid", "Calm", "Mellow", "Tiny", "Swift",
            "Clever", "Bold", "Fuzzy", "Golden", "Mystic", "Vivid", "Witty"
    };

    // ok now we have a zoo here.
    private static final String[] DEFAULT_ANIMALS = {
            "Antelope", "Ape", "Badger", "Bat", "Bear",
            "Bee", "Beetle", "Bison", "Boar", "Buffalo",
            "Bunny", "Camel", "Canary", "Cat", "Cheetah",
            "Cobra", "Condor", "Cougar", "Coyote", "Crane",
            "Crow", "Deer", "Dingo", "Dolphin", "Donkey",
            "Dragonfly", "Duck", "Eagle", "Egret", "Elephant",
            "Elk", "Falcon", "Ferret", "Finch", "Fish",
            "Fox", "Frog", "Gazelle", "Gecko", "Giraffe",
            "Goat", "Goose", "Gorilla", "Grasshopper", "Hawk",
            "Hedgehog", "Heron", "Hippo", "Horse", "Husky",
            "Ibis", "Iguana", "Jaguar", "Jay", "Jellyfish",
            "Kangaroo", "Koala", "Koi", "Lemur", "Leopard",
            "Lion", "Lizard", "Llama", "Lobster", "Lynx",
            "Magpie", "Manatee", "Mantis", "Meerkat", "Mole",
            "Moose", "Mouse", "Narwhal", "Ocelot", "Octopus",
            "Ostrich", "Otter", "Owl", "Panda", "Panther",
            "Parrot", "Peacock", "Pelican", "Penguin", "Pigeon",
            "Porcupine", "Puma", "Quail", "Rabbit", "Raccoon",
            "Ram", "Raven", "Reindeer", "Rhino", "Robin",
            "Salamander", "Seal", "Shark", "Sheep", "Skunk",
            "Sloth", "Snail", "Snake", "Sparrow", "Spider",
            "Swan", "Tiger", "Toad", "Turtle", "Viper",
            "Walrus", "Weasel", "Whale", "Wolf", "Wombat",
            "Yak", "Zebra"
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