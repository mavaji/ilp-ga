package ga.util;

/**
 * A utility class purely to provide a random integer generator
 */
public class RandomInt {
    /**
     * Returns a random integer evenly selected from the range 0 to
     * rangeSize-1.
     *
     * @param rangeSize the number of possible answers wanted
     * @return a random integer in the required range
     */
    public static int next(int rangeSize) {
        int random = (int) (Math.random() * rangeSize);

        // Cope with the 1 in 2^64-ish chance that random returns 1.0

        if (random == rangeSize) {
            --random;
        }

        return random;
    }
}
