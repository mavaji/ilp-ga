package ga.kit;

import ga.util.ExtendedBitSet;
import ga.util.RandomInt;

import java.io.Serializable;

/**
 * An abstract class that represents a chromosome and provides various appropriate
 * ways of constructing and manipulating the chromosome.
 * <p/>
 * The extended classes must implement the <code>fitnessFunction()</code> method to indicate
 * a problem specific chromosome.
 */
public class Chromosome extends ExtendedBitSet
        implements Comparable, Serializable, Cloneable {


    protected Comparable fitness;

    protected double fitnessValue;

    /**
     * Length of the chromosome's genes
     */
    public int geneLength;

    /**
     * Unique id for the chromosome
     */
    protected long thisId;

    /**
     * Unique id generator source
     */
    protected static long lastId = 0;


    /**
     * @return Number of genes in this chromosome
     */
    public int geneNumbers() {
        return (lenBits - 1) / geneLength + 1;
    }

    public int selectGene() {
        return geneLength * RandomInt.next(geneNumbers());
    }

    /**
     * Default constructor
     */
    public Chromosome() {
    }

    /**
     * Creates a random chromosome with a given length.
     *
     * @param length length of chromosome
     */
    public Chromosome(int length) {
        super(length, 0);
        thisId = ++lastId;
    }

    /**
     * Creates a chromosome from an existing set of bits
     *
     * @param bitSet set of bits to use
     */
    public Chromosome(ExtendedBitSet bitSet) {
        super(bitSet);
        thisId = ++lastId;
    }

    /**
     * Creates a chromosome by copying another.
     *
     * @param parent chromosome to copy
     */
    public Chromosome(Chromosome parent) {
        this((ExtendedBitSet) parent);
        this.geneLength = parent.geneLength;
    }

    /**
     * Creates a chromosome from two parent chromosome, randomly selecting
     * bits from each.
     *
     * @param parent1 mom
     * @param parent2 pop
     */
    public Chromosome(Chromosome parent1, Chromosome parent2) {
        this(parent1, parent2, 0.5);
    }

    /**
     * Creates a chromosome from two parent chromosomes, selecting bits
     * from each according to the given crossover rate.  A value of 0.0
     * means all bits come from parent1, 1.0 means that all bits come
     * from parent2.  The child is always of equal length to parent1.
     *
     * @param parent1       mom
     * @param parent2       pop
     * @param crossoverRate value between 0.0 and 1.0 dictating crossover rate
     */
    public Chromosome(Chromosome parent1, Chromosome parent2, double crossoverRate) {
        this(parent1);

        if (crossoverRate > 0) {
            crossover(parent2, crossoverRate);
        }
    }

    /**
     * Creates a chromosome by mutating another.
     *
     * @param parent       chromosome to copy
     * @param mutationRate rate of mutation. 0.0 = no mutation, 1.0 is
     *                     equivalent to inverting the entire chromosome.
     */
    public Chromosome(Chromosome parent, double mutationRate) {
        this(parent);

        if (mutationRate > 0.0) {
            mutate(mutationRate);
        }
    }

    /**
     * Copy the contents of another chromosome
     *
     * @param other the chromosome to copy
     * @return this
     */
    public Chromosome copy(Chromosome other) {
        super.copy((ExtendedBitSet) other);

        return this;
    }

    /**
     * Create and return a copy of this chromosome.
     *
     * @return a copy of this chromosome
     */
    public Object clone() {
        return new Chromosome(this);
    }

    /**
     * Mutate the chromosome with given probability of each bit changing.
     *
     * @param mutationRate bit change probability from 0.0 to 1.0.
     * @return this
     */
    public Chromosome mutate(double mutationRate) {
        for (int i = 0; i < lenBits; ++i) {
            if (mutationRate >= 1.0 || Math.random() < mutationRate) {
                invert(i);
            }
        }

        return this;
    }

    /**
     * Uniform crossover. Performs a uniform crossover of this chromosome
     * with another with given probability of selecting each bit from the
     * other chromosome.
     *
     * @param other         other chromosome
     * @param crossoverRate crossover rate (0.0 <= rate <= 1.0)
     * @return this
     */
    public Chromosome crossover(Chromosome other, double crossoverRate) {
        // Use the shorter of the two

        int limit = (lenBits < other.lenBits) ? lenBits : other.lenBits;

        for (int i = 0; i < limit; ++i) {
            if (crossoverRate >= 1.0 || Math.random() < crossoverRate) {
                setBooleanAt(i, other.get(i));
            }
        }

        return this;
    }

    /**
     * Swap two chunks of bits within the chromosome.  If the chunks
     * overlap the contained values will be extracted correctly, but then
     * chunk1 will be written back to chunk2's position after chunk2 is
     * written to chunk1's position.
     *
     * @param chunk1 Offset of the first chunk
     * @param chunk2 Offset of the second chunk
     * @param length Length of the chunks
     * @return this
     */
    public Chromosome transposeChunk(int chunk1, int chunk2, int length) {
        ExtendedBitSet bs1 = getSubSet(chunk1, length);
        ExtendedBitSet bs2 = getSubSet(chunk2, length);

        setSubSet(chunk1, bs2);
        setSubSet(chunk2, bs1);

        return this;
    }

    /**
     * Adds a chunk of bits at the end.  The bits of the new chunk are
     * randomly initialised.
     *
     * @param length length of chunk to add
     * @return this
     */
    public Chromosome addChunkEnd(int length) {
        ExtendedBitSet bs = new ExtendedBitSet(length, 0);

        insertSubSet(-1, bs);
        return this;
    }

    /**
     * Removes a chunk of bits.  <code>length</code> bits at offset
     * <code>offset</code> are removed.
     *
     * @param offset position of chunk
     * @param length length of chunk to remove
     * @return this
     */
    Chromosome killChunk(int offset, int length) {
        deleteSubSet(offset, length);
        return this;
    }

    /**
     * Duplicate a chunk of bits.  <code>length</code> bits at offset
     * <code>offset</code> are duplicated and the copy is inserted after the
     * original in the chromosome.
     *
     * @param offset position of chunk
     * @param length length of chunk to duplicate
     * @return this
     */
    public Chromosome dupChunk(int offset, int length) {
        ExtendedBitSet bs = getSubSet(offset, length);

        insertSubSet(offset, bs);
        return this;
    }

    /**
     * Compares this chromosome with another one due to the value of their fitness function.
     *
     * @param other The chromosome to be compared
     * @return 1 if this chromosome is better, -1 if worse, 0 if they have the same fitness
     */
    public int compareTo(Object other) {
        Chromosome otherChromosome = (Chromosome) other;

        if (fitness == null) {
            if (fitnessValue > otherChromosome.fitnessValue) {
                return 1;
            } else if (fitnessValue < otherChromosome.fitnessValue) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return fitness.compareTo(((Chromosome) other).fitness);
        }
    }

    /**
     * Checks if this chromosome is equal to another one.
     *
     * @param other The chromosome to be compared
     * @return <code>true</code> if both chromosomes have the same fitness, <code>false</code> else
     */
    public boolean equals(Object other) {
        if (other instanceof Chromosome) {
            return compareTo((Chromosome) other) == 0;
        } else {
            return false;
        }
    }

    public Chromosome setFitness(double fitnessValue) {
        fitness = null;
        this.fitnessValue = fitnessValue;
        return this;
    }

    public Chromosome setFitness(Comparable fitness) {
        this.fitness = fitness;
        return this;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public Comparable getFitness() {
        return fitness;
    }

    public String toString() {
        String s = "[id:" + thisId + ":" + super.toString() + "]";
        if (fitness == null) {
            return "[fitness:" + fitnessValue + "," + s + "]";
        } else {
            return "[" + fitness.toString() + "," + s + "]";
        }
    }
}
