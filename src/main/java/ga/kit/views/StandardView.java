package ga.kit.views;

import ga.kit.Chromosome;

import java.io.Serializable;

/**
 * Classes derived from StandardView are used to impose some interpretation on a
 * chromosome.  The default view (as provided by this base class), simply
 * treats the chromosome as a string of single bit genes, each encoding a
 * boolean.  More useful interpretations can be encoded by subclasses.
 */
public class StandardView implements Serializable {
    public StandardView() {
    }

    /**
     * Get the value of gene <code>index</code>.  The default implementation
     * simply regards the chromosome as n single bit genes and returns the
     * bit value at the given offset encoded as a Boolean.  It is intended
     * to be overridden by subclasses.
     */
    public Object getGene(Chromosome chromosome, int index) {
        return new Boolean(chromosome.get(index));
    }

    /**
     * Return the number of genes.  The default is the same as the number of
     * bits.
     */
    public int numberOfGenes(Chromosome chromosome) {
        return chromosome.size();
    }

    /**
     * Returns a stringified representation of a chromosome as viewed
     * through this view.
     */
    public String toString(Chromosome chromosome) {
        return chromosome.toString();
    }
}
