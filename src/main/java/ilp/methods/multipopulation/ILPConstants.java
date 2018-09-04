package ilp.methods.multipopulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.StringTokenizer;

/**
 * Constants that are used throughout the program.
 *
 * @author Vahid Mavaji
 */
public abstract class ILPConstants {

    /**
     * Token that indicates path of the test case file.
     */
    private static String tkTEST_CASE_PATH = "<path of test case file>";
    /**
     * Token that indicates number of generations in this island.
     */
    private static String tkGENERATIONS = "<generations>";
    /**
     * Token that indicates number of chromosoms in this island.
     */
    private static String tkCHROMS = "<chromosomes>";
    /**
     * Token that indicates IP address of the boot-strap node.
     */
    private static String tkBOOT_STRAP_IP = "<boot-strap IP>";
    /**
     * Token that indicates port on which the boot-strap node receives its initial messages.
     */
    private static String tkBOOT_STRAP_PORT = "<boot-strap port>";
    /**
     * Token that indicates penalty factors values.
     */
    private static String tkPENALTIES = "<penalty factors>";
    /**
     * Token that indicates migration rate of this island's population.
     */
    private static String tkMIGRATION_RATE = "<migration rate>";
    /**
     * Token that indicates number of migration times of this island.
     */
    private static String tkITERATIONS = "<iterations>";
    /**
     * Token that indicates communication port of this island.
     */
    private static String tkPORT = "<port>";
    /**
     * Token that indicates end of configuration file.
     */
    private static String tkEND = "<end of config>";

    /**
     * Number of constraints in the ILP equation.
     */
    public static int NUM_CONSTRAINTS = 0;
    /**
     * Size of a gene.
     */
    public static int SIZE_GENES = 1;
    /**
     * Maximum number of bits a float number has in a gene.
     */
    public static int FLOAT_WIDTH = 16;
    /**
     * Range of float numbers encapsulated in genes.
     */
    public static double RANGE = (double) (1L << (FLOAT_WIDTH - 4)) - 1;

    /**
     * Path of the configuration file.
     */
    public static String CONFIG_FILE;
    /**
     * Path of the test case file.
     */
    public static String TEST_CASE_FILE;
    /**
     * Number of generations used by this island.
     */
    public static int GENERATIONS;
    /**
     * Number of chromosomes in each generation uses by this island.
     */
    public static int NUM_CHROMS;
    /**
     * IP address of the boot-strap node.
     */
    public static String BOOT_STRAP_IP;
    /**
     * Port number of the boot-strap node.
     */
    public static int BOOT_STRAP_PORT;
    /**
     * Penalty factors used in computing fitness values.
     */
    public static int[] PENALTIES;
    /**
     * Migration rate of this island's population.
     */
    public static float MIGRATION_RATE;
    /**
     * Number of migration times of this island.
     */
    public static int ITERATIONS;
    /**
     * Communication port of this island.
     */
    public static int PORT;
    /**
     * IP of this island
     */
    public static String IP;

    /**
     * Indicates that the message has no tag.
     */
    public static int tagNIL = 00;
    /**
     * Message that an island sends to the bootstrap for being a member of the network.
     */
    public static int tagADD_ME = -11;
    /**
     * Message that an island sends to the bootstrap for being removed from the network.
     */
    public static int tagREMOVE_ME = -22;
    /**
     * Message that an island sends to the bootstrap to be broadcast to all of the network's islands.
     */
    public static int tagBROADCAST_ME = -33;
    /**
     * Message that the boot-strap send to an island says you are accepted to the network.
     */
    public static int tagACCEPTED = -44;
    /**
     * Message that the boot-strap send to an island says you are removed from the network.
     */
    public static int tagREMOVED = -55;
    /**
     * Message that the boot-strap send to an island says there are some new immigrants.
     */
    public static int tagNEW_IMMIGRANTS = -66;
    /**
     * Message that the boot-strap send to an island containing island pool.
     */
    public static int tagISLAND_POOL = -77;

    /**
     * State of being connected to the network.
     */
    public static int stNOT_CONNECTED = -111;
    /**
     * State of not being connected to the network.
     */
    public static int stCONNECTED = -222;

    public static void init() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE));
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.contains(tkTEST_CASE_PATH)) {
                    line = reader.readLine();
                    TEST_CASE_FILE = line;
                } else if (line.contains(tkGENERATIONS)) {
                    line = reader.readLine();
                    GENERATIONS = Integer.valueOf(line).intValue();
                } else if (line.contains(tkCHROMS)) {
                    line = reader.readLine();
                    NUM_CHROMS = Integer.valueOf(line).intValue();
                } else if (line.contains(tkBOOT_STRAP_IP)) {
                    line = reader.readLine();
                    BOOT_STRAP_IP = line;
                } else if (line.contains(tkBOOT_STRAP_PORT)) {
                    line = reader.readLine();
                    BOOT_STRAP_PORT = Integer.valueOf(line).intValue();
                } else if (line.contains(tkPENALTIES)) {
                    line = reader.readLine();
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    NUM_CONSTRAINTS = tokenizer.countTokens();
                    PENALTIES = new int[NUM_CONSTRAINTS];
                    for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                        PENALTIES[i] = Integer.valueOf(tokenizer.nextToken()).intValue();
                    }
                } else if (line.contains(tkMIGRATION_RATE)) {
                    line = reader.readLine();
                    MIGRATION_RATE = Float.valueOf(line).floatValue();
                } else if (line.contains(tkITERATIONS)) {
                    line = reader.readLine();
                    ITERATIONS = Integer.valueOf(line).intValue();
                } else if (line.contains(tkPORT)) {
                    line = reader.readLine();
                    PORT = Integer.valueOf(line).intValue();
                } else if (line.contains(tkEND)) {
                    break;
                }
            }
            reader.close();

            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
