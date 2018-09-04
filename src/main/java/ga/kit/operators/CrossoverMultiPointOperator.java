package ga.kit.operators;

import ga.kit.Chromosome;
import ga.util.RandomInt;

/**
 * A class that represents a binary genetic operator that performs an n
 * point crossover between two chromosomes. Gene boundaries are not
 * respected.
 */
public class CrossoverMultiPointOperator extends GeneticOperator {
    protected int[] crossoverPoints;

    public CrossoverMultiPointOperator(int numberOfPoints) {
        super(true);

        // Two extra elements to hold zero at the beginning and max length
        // at the end to provide fixed endpoints to the crossovers.

        crossoverPoints = new int[((numberOfPoints >= 1) ? numberOfPoints : 1) + 2];
        crossoverPoints[0] = 0;
    }

    public Chromosome apply(Chromosome chromosome1, Chromosome chromosome2) {
        // Compute minimum length

        int minLen = (chromosome1.size() < chromosome2.size()) ? chromosome1.size() : chromosome2.size();


        crossoverPoints[crossoverPoints.length - 1] = minLen - 1;

        // Generate crossover points. The chromosomes is divided more or
        // less regularly

        int divLen = minLen / (crossoverPoints.length - 1);

        for (int i = 1; i < crossoverPoints.length - 1; ++i) {
            // To avoid making 2 points the same

            crossoverPoints[i] = crossoverPoints[i - 1] + 1 +
                    RandomInt.next((i + 1) * divLen - 1 - crossoverPoints[i - 1]);
        }

        // Do Crossover

        for (int i = 1; i <= crossoverPoints.length - 1; i += 2) {
            if (crossoverPoints[i] < minLen) {
                chromosome1.swapSubSet(crossoverPoints[i - 1],
                        crossoverPoints[i] - crossoverPoints[i - 1], chromosome2);
            }
        }

        return chromosome1;
    }

}
