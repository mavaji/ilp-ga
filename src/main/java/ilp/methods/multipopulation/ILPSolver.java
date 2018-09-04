package ilp.methods.multipopulation;

import ga.kit.Chromosome;
import ga.kit.Population;
import ga.kit.operators.CrossoverMultiPointOperator;
import ga.kit.operators.CrossoverSinglePointOperator;
import ga.kit.operators.CrossoverUniformOperator;
import ga.kit.operators.ShiftOperator;
import ga.kit.views.FloatFixedGeneView;
import ga.kit.views.IntegerFixedGeneView;
import ilp.util.TestCaseData;
import ilp.util.TestInputReader;

import java.util.Iterator;

/**
 * This class is used to solve a set of ilp equations.
 *
 * @author Vahid Mavaji
 */
public class ILPSolver {
    /**
     * The input data that the solution is based on.
     */
    private TestCaseData testCaseData;
    /**
     * Matrix indicating that which task is running on which core.
     */
    private int[][] x;
    /**
     * Matrix indicating that whether a core is allocated or not.
     */
    private int[] p;
    /**
     * Matrix indicating start time of each task.
     */
    private double[] s;
    /**
     * Matrix indicating runtime of each task.
     */
    private double[] rt;
    /**
     * if <code>y[i][j] = 1</code> then <code>s[i] <= s[j]</code>.
     */
    private int[][] y;
    /**
     * if <code>w[i][j] = 1</code> then core <code>c[i]</code> is connected to the test bus <code>TB[j]</code>.
     */
    private int[][] w;
    /**
     * Critical path delay.
     */
    private double RT;
    /**
     * <code>T[i][j]</code> gives test time for core <code>i</code>
     * if it is connected to test bus <code>j</code>.
     */
    private int[][] T;
    /**
     * Number of Genes in a chromosome: x[i][j] and s[i] and w[i][j] and testtime are included in a chromosome.
     */
    private int numGenes;
    /**
     * Contains a set of results for a given ILP.
     */
    public Population people;
    /**
     * Area of the SOC.
     */
    public double AREA;
    /**
     * Power consumption of the SOC.
     */
    public double POWER;
    /**
     * Total test time.
     */
    public int testTime;
    /**
     * <code>c[i]</code> indicates how much constraint #i is not satisfied.
     * That is this matrix is a measure of badness of a given solution.
     */
    public int[] c = new int[ILPConstants.NUM_CONSTRAINTS];

    /**
     * Constructor method.
     */
    public ILPSolver() {
        testCaseData = new TestInputReader(ILPConstants.TEST_CASE_FILE).getTestCaseData();
        init();
    }

    /**
     * Initializes each matrix and data that is needed for computations.
     */
    public void init() {

        x = new int[testCaseData.n][testCaseData.m];
        p = new int[testCaseData.m];
        s = new double[testCaseData.n];
        rt = new double[testCaseData.n];
        y = new int[testCaseData.n][testCaseData.n];
        w = new int[testCaseData.m][testCaseData.NB];
        T = new int[testCaseData.m][testCaseData.NB];

        numGenes = ((testCaseData.n * testCaseData.m / ILPConstants.FLOAT_WIDTH) + 1) * ILPConstants.FLOAT_WIDTH
                + testCaseData.n * ILPConstants.FLOAT_WIDTH
                + ((testCaseData.m * testCaseData.NB / ILPConstants.FLOAT_WIDTH) + 1) * ILPConstants.FLOAT_WIDTH ;
//                + ILPConstants.FLOAT_WIDTH;

        // computing T_ij
        for (int i = 0; i < testCaseData.m; i++) {
            for (int j = 0; j < testCaseData.NB; j++) {
                if (testCaseData.C[i].phi() <= testCaseData.TB[j].testLength) {
                    T[i][j] = testCaseData.C[i].t;
                } else {
                    T[i][j] = (testCaseData.C[i].phi() - testCaseData.TB[j].testLength + 1) * testCaseData.C[i].t;
                }
            }
        }

        people = new Population(ILPConstants.NUM_CHROMS, ILPConstants.SIZE_GENES, numGenes, numGenes, 10.0 / (ILPConstants.SIZE_GENES * numGenes));
    }

    /**
     * Set the values of matrices and other data that is used during computation.
     * It is used when data need to be updated.
     */
    private void updateValues() {

        // computing p_i
        for (int j = 0; j < p.length; j++) {
            int sum = 0;
            for (int i = 0; i < testCaseData.n; i++) {
                sum += x[i][j];
            }
            p[j] = (sum >= 1) ? 1 : 0;
        }

        // computing rt_i and RT.
        RT = 0;
        for (int i = 0; i < rt.length; i++) {

            if (!testCaseData.T[i].isCommunication) {
                double sum = 0;
                for (int j = 0; j < testCaseData.m; j++) {
                    sum += x[i][j] * testCaseData.ex[i][j];
                }
                rt[i] = sum;
            } else {
                for (int j = 0; j < testCaseData.n; j++) {
                    for (int k = 0; k < testCaseData.n; k++) {
                        if (testCaseData.PR[k][i] == 1 && testCaseData.PR[i][j] == 1) {
                            double sum = 0;
                            double sum1 = 1;
                            for (int r = 0; r < testCaseData.m; r++) {
                                sum += x[i][r] * testCaseData.ex[i][r];
                                sum1 += x[k][r] * x[j][r];
                            }
                            rt[i] = sum * (1 - sum1);
                        }
                    }
                }
            }
            RT += rt[i];
        }

        // computing y_ij
        for (int i = 0; i < testCaseData.n; i++) {
            for (int j = 0; j < testCaseData.n; j++) {
                y[i][j] = (s[i] <= s[j]) ? 1 : 0;
            }
        }

        // computing testtime for each test bus.
        for (int j = 0; j < testCaseData.NB; j++) {
            int sum = 0;
            for (int i = 0; i < testCaseData.m; i++) {
                sum += w[i][j] * T[i][j];
            }
            testCaseData.TB[j].testTime = sum;
        }

        // computing the Area of the SOC.
        AREA = 0;
        for (int j = 0; j < testCaseData.m; j++) {
            AREA += p[j] * testCaseData.C[j].area;
        }

        // computing the Power consumption of the SOC.
        POWER = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int j = 0; j < testCaseData.m; j++) {
                POWER += x[i][j] * testCaseData.pw[i][j];
            }
        }
    }

    /**
     * Computes fitness of a chromosome(a given result).
     *
     * @param chrom the chromosome which it's fitness is to be computed
     * @return the fitness of its input parameter
     */
    public double computeFitness(Chromosome chrom) {
        FloatFixedGeneView ffgv = new FloatFixedGeneView(ILPConstants.FLOAT_WIDTH, 0, ILPConstants.RANGE);
        IntegerFixedGeneView ifgv = new IntegerFixedGeneView(ILPConstants.SIZE_GENES, false);

        for (int i = 0; i < testCaseData.n; i++) {
            for (int j = 0; j < testCaseData.m; j++) {
                x[i][j] = ((Long) ifgv.getGene(chrom, i * testCaseData.m + j)).intValue();
            }
        }
        for (int i = 0; i < testCaseData.n; i++) {
            s[i] = ((Double) ffgv.getGene(chrom, 1 + (testCaseData.n * testCaseData.m / ILPConstants.FLOAT_WIDTH) + i)).doubleValue();
        }
        for (int i = 0; i < testCaseData.m; i++) {
            for (int j = 0; j < testCaseData.NB; j++) {
                w[i][j] = ((Long) ifgv.getGene(chrom,
                        ((testCaseData.n * testCaseData.m / ILPConstants.FLOAT_WIDTH) + 1) * ILPConstants.FLOAT_WIDTH
                        + testCaseData.n * ILPConstants.FLOAT_WIDTH + i * testCaseData.NB + j)).intValue();
            }
        }
//        testTime = ((Double) ffgv.getGene(chrom,
//                1 + (testCaseData.n * testCaseData.m / ILPConstants.FLOAT_WIDTH)
//                + testCaseData.n
//                + 1 + (testCaseData.m * testCaseData.NB / ILPConstants.FLOAT_WIDTH))).intValue();

        updateValues();

        // fitness #1
        c[0] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            int sum = 0;
            for (int j = 0; j < testCaseData.m; j++) {
                sum += x[i][j];
            }
            if (sum != 1) {
                c[0] += ILPConstants.PENALTIES[0];
            }
        }

        // fitness #2
        c[1] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int j = 0; j < testCaseData.m; j++) {
                if (!(x[i][j] <= testCaseData.a[i][j])) {
                    c[1] += ILPConstants.PENALTIES[1];
                }
            }
        }

        //fintness #3
        c[2] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int k = 0; k < testCaseData.n; k++) {
                if (i != k && !((s[k] - s[i]) <= (y[i][k] * RT))) {
                    c[2] += ILPConstants.PENALTIES[2];
                }
            }
        }

        // fitness #4
        c[3] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int k = i + 1; k < testCaseData.n; k++) {
                if (y[i][k] + y[k][i] != 1) {
                    c[3] += ILPConstants.PENALTIES[3];
                }
            }
        }

        //fitness #5
        c[4] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int k = 0; k < testCaseData.n; k++) {
                if (i != k) {
                    for (int j = 0; j < testCaseData.m; j++) {
                        if (!((s[i] + rt[i] - s[k]) <= RT * (1 - y[i][k] + 2 - x[i][j] - x[k][j]))) {
                            c[4] += ILPConstants.PENALTIES[4];
                        }
                    }
                }
            }
        }

        //fitness #6
        c[5] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int k = 0; k < testCaseData.n; k++) {
                if (testCaseData.PR[i][k] == 1 && y[i][k] != 1) {
                    c[5] += ILPConstants.PENALTIES[5];
                }
            }
        }

        //fitness #7
        c[6] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            for (int k = 0; k < testCaseData.n; k++) {
                if (testCaseData.PR[i][k] == 1 && !((s[i] + rt[i] - s[k]) <= 0)) {
                    c[6] += ILPConstants.PENALTIES[6];
                }
            }
        }

        //fitness #8
        c[7] = 0;
        for (int i = 0; i < testCaseData.n; i++) {
            if (testCaseData.T[i].deadLine != -1 && !((s[i] + rt[i]) <= testCaseData.T[i].deadLine)) {
                c[7] += ILPConstants.PENALTIES[7];
            }
        }

        //fitness #9
        c[8] = 0;
        for (int i = 0; i < testCaseData.m; i++) {
            int sum = 0;
            for (int j = 0; j < testCaseData.NB; j++) {
                sum += w[i][j];
            }
            if (sum != p[i]) {
                c[8] += ILPConstants.PENALTIES[8];
            }
        }

        //fitness #10
        c[9] = 0;
        for (int j = 0; j < testCaseData.NB; j++) {
            if (testTime < testCaseData.TB[j].testTime) {
                testTime = testCaseData.TB[j].testTime;
            }
        }

        double sum = 1;
        for (int i = 0; i < ILPConstants.NUM_CONSTRAINTS; i++) {
            if (c[i] != 0) {
                sum += c[i];
            }
        }


        double fitness = Double.MAX_VALUE - (testTime * AREA * POWER);
        fitness = 10 * Math.log10(fitness);

        if (sum != 1) {
            fitness /= (100 * sum);
        }

        return fitness;
    }

    /**
     * Tries to solve an ILP problem using genetic algorithms.
     * After calling this method, the member <code>people</code> contains the set of results.
     */
    public void solve() {

        CrossoverSinglePointOperator cspo = new CrossoverSinglePointOperator();
        CrossoverUniformOperator cuo = new CrossoverUniformOperator(0.1);
        CrossoverMultiPointOperator cmpo = new CrossoverMultiPointOperator(numGenes / 20);
        ShiftOperator so = new ShiftOperator();

        people.setEliteRate(0.3);
        people.setCullRate(0.4);
        people.addOperator(cspo, 3);
        people.addOperator(cuo, 2);
        people.addOperator(cmpo, 2);
        people.addOperator(so, 1);

        for (int i = 0; i < ILPConstants.GENERATIONS; i++) {
            for (Iterator iter = people.iterator(); iter.hasNext();) {
                Chromosome chrom = (Chromosome) iter.next();
                chrom.setFitness(computeFitness(chrom));
            }

            if (i < ILPConstants.GENERATIONS - 1) {
                people.newGeneration();
            }
        }
    }
}
