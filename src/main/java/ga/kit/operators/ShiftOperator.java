package ga.kit.operators;

import ga.kit.Chromosome;
import ga.util.RandomInt;

/**
 * A class that represents a unary genetic operator that randomly adds or
 * subtracts one from the value of a random gene in a chromosome and does
 * the opposite to the next gene in the chromosome.
 */
public class ShiftOperator extends GeneticOperator {

    public ShiftOperator() {
        super(false);
    }

    public Chromosome apply(Chromosome chromosome) {
        return applyAt(RandomInt.next(chromosome.geneNumbers() - 1) * chromosome.geneLength, chromosome);
    }

    public Chromosome applyAt(int index, Chromosome chromosome) {

        // Needs at least two genes

        if (chromosome.geneNumbers() > 1) {
            index -= index % chromosome.geneLength;

            if (index <= chromosome.size() - (chromosome.geneLength * 2)) {
                long value1 = chromosome.getLongAt(index, chromosome.geneLength);
                long value2 = chromosome.getLongAt(index + chromosome.geneLength, chromosome.geneLength);

                if (Math.random() < 0.5) {
                    --value1;
                    ++value2;
                } else {
                    ++value2;
                    --value1;
                }

                chromosome.setLongAt(index, chromosome.geneLength, value1);
                chromosome.setLongAt(index + chromosome.geneLength, chromosome.geneLength, value2);
            }
        }

        return chromosome;
    }

}
