package ilp.util;


/**
 * This class represents data of a test case for an ilp equations.
 *
 * @author Vahid Mavaji
 */
public class TestCaseData {

    /**
     * Number of Tasks.
     */
    public int n;
    /**
     * Array of Tasks.
     */
    public Task[] T;
    /**
     * Number of Cores.
     */
    public int m;
    /**
     * Array of Cores.
     */
    public Core[] C;
    /**
     * Number of Test Busses.
     */
    public int NB;
    /**
     * Array of Test Busses.
     */
    public TestBus[] TB;
    /**
     * Task Graph Adjacency matrix.
     */
    public int[][] TG;
    /**
     * Matrix indicating possiblity of execution of each Task on each Core.
     */
    public int[][] a;
    /**
     * Matrix indicating execution time of each Task on each Core.
     */
    public double[][] ex;
    /**
     * Matrix indicating power consumption of each Task on each Core.
     */
    public double[][] pw;
    /**
     * Precedence Relationship matix.
     */
    public int[][] PR;


    public void init() {
        T = new Task[n];
        for (int i = 0; i < n; i++) {
            T[i] = new Task();
        }
        C = new Core[m];
        for (int i = 0; i < m; i++) {
            C[i] = new Core();
        }
        TB = new TestBus[NB];
        for (int i = 0; i < NB; i++) {
            TB[i] = new TestBus();
        }
        TG = new int[n][n];
        a = new int[n][m];
        ex = new double[n][m];
        pw = new double[n][m];
        PR = new int[n][n];
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("n = " + n + "\n");
        buffer.append("m = " + m + "\n");
        buffer.append("NB = " + NB + "\n");
        buffer.append("\nTG_ij = \n[\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buffer.append(TG[i][j] + " ");
            }
            buffer.append("\n");
        }
        buffer.append("]\n");
        buffer.append("\nCommunication IDs =\n{");
        for (int i = 0; i < n; i++) {
            buffer.append((T[i].isCommunication ? "1" : "0") + " ");
        }
        buffer.append("}\n\nTask deadlines = \n[");
        for (int i = 0; i < n; i++) {
            buffer.append(T[i].deadLine + " ");
        }
        buffer.append("]\n\nCore bus IDs = \n{");
        for (int i = 0; i < m; i++) {
            buffer.append((C[i].isBus ? "1" : "0") + " ");
        }
        buffer.append("}\n\nArea of each core = \n[");
        for (int i = 0; i < m; i++) {
            buffer.append(C[i].area + " ");
        }
        buffer.append("]\n\nPrice of each core = \n[");
        for (int i = 0; i < m; i++) {
            buffer.append(C[i].price + " ");
        }
        buffer.append("]\n\nInput number of each core = \n[");
        for (int i = 0; i < m; i++) {
            buffer.append(C[i].n + " ");
        }
        buffer.append("]\n\nOutput number of each core = \n[");
        for (int i = 0; i < m; i++) {
            buffer.append(C[i].m + " ");
        }
        buffer.append("]\n\na_ij = \n[\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                buffer.append(a[i][j] + " ");
            }
            buffer.append("\n");
        }
        buffer.append("]\n\nex_ij = \n[\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                buffer.append(ex[i][j] + " ");
            }
            buffer.append("\n");
        }
        buffer.append("]\n\npw_ij = \n[\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                buffer.append(pw[i][j] + " ");
            }
            buffer.append("\n");
        }
        buffer.append("]\n\nLength of test busses = \n[");
        for (int i = 0; i < NB; i++) {
            buffer.append(TB[i].testLength + " ");
        }
        buffer.append("]\n\nArea of test busses = \n[");
        for (int i = 0; i < NB; i++) {
            buffer.append(TB[i].area + " ");
        }
        buffer.append("]\n\nScan length of each core = \n[");
        for (int i = 0; i < m; i++) {
            buffer.append(C[i].t + " ");
        }
        buffer.append("]\n\nPR_ij = \n[\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                buffer.append(PR[i][j] + " ");
            }
            buffer.append("\n");
        }
        buffer.append("\n]");

        return buffer.toString();
    }
}
