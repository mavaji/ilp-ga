package ga.kit.operators;

import ga.kit.Chromosome;

import java.io.Serializable;

/**
 * A class that represents a genetic operator that can manipulate a
 * chromosome.
 */
public class GeneticOperator implements Comparable, Serializable {
    protected boolean isBinaryOperation;
    protected double weight;

    public GeneticOperator(double weight) {
        this.weight = weight;
    }

    public GeneticOperator(boolean isBinaryOperation) {
        this.isBinaryOperation = isBinaryOperation;
    }

    public Chromosome apply(Chromosome chrom1) {
        return chrom1;
    }

    public Chromosome apply(Chromosome chrom1, Chromosome chrom2) {
        return apply(chrom1);
    }

    public Chromosome applyAt(int index, Chromosome chrom1) {
        return chrom1;
    }

    public Chromosome applyAt(int index, Chromosome chrom1, Chromosome chrom2) {
        return applyAt(index, chrom1);
    }

    /**
     * Whether or not the operator is binary
     */
    public boolean isBinaryOperation() {
        return isBinaryOperation;
    }

    public int compareTo(Object other) {
        GeneticOperator otherGeneticOperator = (GeneticOperator) other;

        if (weight > otherGeneticOperator.weight) {
            return 1;
        } else if (weight < otherGeneticOperator.weight) {
            return -1;
        } else {
            return 0;
        }
    }

    public GeneticOperator setWeight(double weight) {
        this.weight = weight;
        return this;
    }

    public double getWeight() {
        return weight;
    }
}
