package ga.kit.operators;

import ga.kit.Chromosome;

/**
 * A class that represents a binary genetic operator that performs a single
 * gene crossover between two chromosomes.
 */
public class CrossoverSinglePointOperator extends GeneticOperator {

    public CrossoverSinglePointOperator() {
        super(true);
    }

    public Chromosome apply(Chromosome chromosome1, Chromosome chromosome2) {
        Chromosome shorter = (chromosome1.size() < chromosome2.size()) ? chromosome1 : chromosome2;

        return applyAt(shorter.selectGene(), chromosome1, chromosome2);
    }

    public Chromosome applyAt(int index, Chromosome chromosome1, Chromosome chromosome2) {
        index -= index % chromosome1.geneLength;

        return (Chromosome) chromosome1.swapSubSet(index, chromosome1.geneLength, chromosome2);
    }
}
