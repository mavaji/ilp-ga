package ga.kit.operators;

import ga.kit.Chromosome;

/**
 * A class that represents a binary genetic operator that performs a random
 * bitwise crossover between two chromosomes.
 */
public class CrossoverUniformOperator extends GeneticOperator {
    protected double crossoverRate;

    public CrossoverUniformOperator(double crossoverRate) {
        super(true);
        this.crossoverRate = crossoverRate;
    }

    public Chromosome apply(Chromosome chromosome1, Chromosome chromosome2) {
        return chromosome1.crossover(chromosome2, crossoverRate);
    }
}
