package ga.kit;


import ga.kit.operators.CrossoverMultiPointOperator;
import ga.kit.operators.GeneticOperator;
import ga.kit.operators.MutateOperator;
import ga.util.RandomInt;
import ga.util.SimpleList;
import ga.util.SortedList;

import java.io.Serializable;
import java.util.ListIterator;

/**
 * A basic class for representing a population of chromosomes.
 */
public class Population extends SimpleList implements Serializable {
    /**
     * Percentage of population carried forward unchanged from each
     * generation.
     */
    protected double elitePercent;

    /**
     * Percentage deemed unfit for reproduction
     */
    protected double cullPercent;

    /**
     * Operators used to modify the current generation when breeding the
     * next.
     */
    protected SortedList operatorList;

    /**
     * Index in our list of the first element that has been generated from
     * new for this generation rather than copied from the previous.
     */
    protected int newIndex;

    /**
     * Cache of the operators in the above list for efficiency, along with
     * details of their weightings to allow one to be selected.
     */
    protected GeneticOperator[] operators;
    protected double[] cumulativeWeight;
    protected double totalWeight;

    public Population() {
    }

    public Population(int populationSize, int geneSize, int minGenes,
                      int maxGenes) {
        this(populationSize, geneSize, minGenes, maxGenes, 0);
    }

    /**
     * Constructor.  If a non-zero mutation rate is given, the default
     * operators of mutation (at the given rate) and two point crossover are
     * associated with the population automatically.
     */
    public Population(int populationSize, int geneSize, int minGenes,
                      int maxGenes, double mutationRate) {
        createPopulation(populationSize, geneSize, minGenes, maxGenes);

        newIndex = 0;
        operators = null;
        cumulativeWeight = null;
        totalWeight = 0.0;
        operatorList = new SortedList();

        // Create default genetic operators, mutation and 2-pt xOver

        if (mutationRate != 0.0) {
            GeneticOperator mutator = new MutateOperator(mutationRate).setWeight(1);
            GeneticOperator crossover = new CrossoverMultiPointOperator(2).setWeight(1); // 2-pt xOver

            // 1 is the default "priority"

            operatorList.add(mutator);
            operatorList.add(crossover);
        }
    }

    /**
     * Empty the population.
     */
    public void clear() {
        // Overridden to reset the new items pointer.

        super.clear();
        newIndex = 0;
    }

    /**
     * Return an iterator over the whole population.
     */
    public ListIterator iteratorAll() {
        return listIterator();
    }

    /**
     * Return an iterator over the portion of the population that is new for
     * this generation.  That is, the portion that has not yet been evaluated
     * for fitness.
     */
    public ListIterator iteratorNewOnly() {
        return listIterator(newIndex);
    }

    /**
     * Provide a genetic modifier operator for use on this population, and
     * the weighting it has for the chances of it being selected.
     */
    public void addOperator(GeneticOperator geneticOperator, double weight) {
        operatorList.add(geneticOperator.setWeight(weight));
        operators = null;
    }

    /**
     * Define the proportion of the population carried forward unchanged
     * between generations.
     */
    public void setEliteRate(double percent) {
        if (percent < 0.0) {
            elitePercent = 0.0;
        } else if (percent > 1.0) {
            elitePercent = 1.0;
        } else {
            elitePercent = percent;
        }
    }

    /**
     * Define the proportion of the population carried forward unchanged
     * between generations.
     */
    public void setCullRate(double percent) {
        if (percent < 0.0) {
            cullPercent = 0.0;
        } else if (percent > 1.0) {
            cullPercent = 1.0;
        } else {
            cullPercent = percent;
        }
    }

    /**
     * A new generation is created by carrying over an elite part unchanged
     * and then generating the remainder from breeding, excluding the worst
     * of the current generation from breeding at a rate defined by
     * setCullRate.
     */
    public void newGeneration() {
        // Create new population by copying the elite and then generating
        // the required remainder.

        SortedList orderedPopulation = new SortedList(this);
        Chromosome[] oldPopulation = new Chromosome[orderedPopulation.size()];
        int totalPopulation = oldPopulation.length;
        int elite = (int) (totalPopulation * elitePercent);
        int culled = (int) (totalPopulation * cullPercent);


        oldPopulation = (Chromosome[]) orderedPopulation.toArray(oldPopulation);
        clear();

        // Chromosomes for the new generation are created by duplication
        // rather than just being transferred to ensure that they remain
        // unchanged by the breeding program carried out later

        for (int i = totalPopulation - elite; i < totalPopulation; ++i) {
            add(new Chromosome(oldPopulation[i]));
        }
        newIndex = elite;

        for (int i = elite; i < totalPopulation; ++i) {
            Chromosome newChromosome = new Chromosome(selectChromosome(oldPopulation, culled,
                    totalPopulation - culled));
            GeneticOperator operator = selectOperator();

            // Apply genetic operator to chosen chromosome


            if (!operator.isBinaryOperation()) {
                operator.apply(newChromosome);
            } else {
                // choose another random gene

                Chromosome mate = new Chromosome(selectChromosome(oldPopulation, culled,
                        totalPopulation - culled));


                operator.apply(newChromosome, mate);
            }


            add(newChromosome);
        }
    }

    /**
     * Eliminate the current population and create a new one
     */
    public void reset(int geneSize, int minGenes, int maxGenes) {
        int totalPopulation = size();

        clear();
        createPopulation(totalPopulation, geneSize, minGenes, maxGenes);
    }

    /**
     * Create new population
     */
    protected void createPopulation(int populationSize, int geneSize, int minGenes,
                                    int maxGenes) {
        for (int i = 0; i < populationSize; ++i) {
            int len = minGenes + RandomInt.next(maxGenes - minGenes + 1);
            Chromosome newChromosome = new Chromosome(len * geneSize);
            newChromosome.geneLength = geneSize;

            add(newChromosome);
        }
    }

    /**
     * Randomly select a chromosome to breed with.  The routine selects a
     * chromosome from <code>chroms</code> from within the range defined by
     * <code>first</code> and <code>length</code>.  This implementation
     * simply selects a valid chromosome at random, however the routine
     * could be overridden by subclasses, e.g. to bias the selection
     * according to the weighting of the chromosomes, vary the algorithm
     * according to the maturity of the population and so on.
     *
     * @param chromosoms array of ChromItems to select a chromosome from.  The
     *                   array is guaranteed to be ordered from worst to best fitness.
     * @param first      the first valid element in the array
     * @param length     the number of valid elements in the array
     * @return a chromosome chosen from the valid elements
     */
    protected Chromosome selectChromosome(Chromosome[] chromosoms, int first,
                                          int length) {
        return chromosoms[RandomInt.next(length) + first];
    }

    /**
     * Randomly select a genetic operator from those associated with this
     * population.
     */
    protected GeneticOperator selectOperator() {
        if (operators == null || operators.length != operatorList.size()) {
            // Create the cache if it's currently invalid

            if (operators == null) {
                operators = new GeneticOperator[operatorList.size()];
            }
            operators = (GeneticOperator[]) operatorList.toArray(operators);
            cumulativeWeight = new double[operators.length];
            totalWeight = 0;

            for (int i = 0; i < operators.length; ++i) {
                double opWeight = operators[i].getWeight();

                cumulativeWeight[i] = opWeight + totalWeight;
                totalWeight += opWeight;
            }
        }

        // generate random operator choice based on the weightings of each
        // operator

        double chosenWeight = Math.random() * totalWeight;
        int min = 0;
        int max = operators.length - 1;
        int chosen = (int) ((chosenWeight / totalWeight) * max);

        do {
            if (chosenWeight > cumulativeWeight[chosen]) {
                min = chosen + 1;
                chosen += (max - chosen - 1) / 2 + 1;
            } else if (chosen != 0 &&
                    chosenWeight <= cumulativeWeight[chosen - 1]) {
                max = chosen - 1;
                chosen -= (chosen - min - 1) / 2 + 1;
            } else {
                break;
            }
        } while (true);

        return operators[chosen];
    }
}
