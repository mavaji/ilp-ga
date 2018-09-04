package ga.kit.operators;

import ga.kit.Chromosome;

/**
 * A class that represents a unary genetic operator that mutates a
 * chromosome at a given rate.
 */
public class MutateOperator extends GeneticOperator {
    protected double mutationRate;

    public MutateOperator(double mutationRate) {
        super(false);
        this.mutationRate = mutationRate;
    }

    public Chromosome apply(Chromosome chrom1) {
        return chrom1.mutate(mutationRate);
    }
}
