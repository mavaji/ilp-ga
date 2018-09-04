package ilp.util;

/**
 * This class represents a core in the ilp equations.
 *
 * @author Vahid Mavaji
 */
public class Core {
    /**
     * Flag indicating that this Core is a bus or not.
     */
    public boolean isBus = false;
    /**
     * Area of this Core.
     */
    public double area = 0;
    /**
     * Price of this Core.
     */
    public double price = 0;
    /**
     * Number of inputs to this core.
     */
    public int n = 0;
    /**
     * Number of outputs from this core.
     */
    public int m = 0;
    /**
     * Scan length of this Core.
     */
    public int t = 0;

    /**
     * @return maximum value between inputs and outputs of this core.
     */
    public int phi() {
        return (m > n) ? m : n;
    }
}
