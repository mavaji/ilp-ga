package ilp.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * This class reads test input data and initializes data of the ilp.
 *
 * @author Vahid Mavaji
 */
public class TestInputReader {
    private String fileName;
    private BufferedReader reader;
    private TestCaseData testCaseData = new TestCaseData();

    public TestInputReader(String fileName) {
        this.fileName = fileName;
    }

    public TestCaseData getTestCaseData() {
        testCaseData = read();
        buildPrecedenceRelationshipMatrix();
        return testCaseData;
    }

    private TestCaseData read() {
        String s;
        StringTokenizer tokenizer;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            // reading Task number
            testCaseData.n = Integer.valueOf(reader.readLine()).intValue();
            // reading Core number
            testCaseData.m = Integer.valueOf(reader.readLine()).intValue();
            //reading Test Bus number
            testCaseData.NB = Integer.valueOf(reader.readLine()).intValue();

            testCaseData.init();

            for (int i = 0; i < 4; i++) {
                reader.readLine();
            }

            // reading Task graph adjacency matrix
            for (int i = 0; i < testCaseData.n; i++) {
                s = reader.readLine();
                tokenizer = new StringTokenizer(s);
                for (int j = 0; tokenizer.hasMoreTokens(); j++) {
                    testCaseData.TG[i][j] = Integer.valueOf(tokenizer.nextToken()).intValue();
                }
            }

            reader.readLine();
            reader.readLine();

            // reading Communication IDs, shows which task is a communication task
            s = reader.readLine();
            s = s.replaceAll("\\{", "").replaceAll(",", "").replaceAll("\\}", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            while (tokenizer.hasMoreTokens()) {
                int index = Integer.valueOf(tokenizer.nextToken()).intValue();
                testCaseData.T[index].isCommunication = true;
            }

            reader.readLine();

            // reading Task deadlines, shows which task has a deadline
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.T[i].deadLine = Double.valueOf(tokenizer.nextToken()).doubleValue();
            }

            reader.readLine();

            // reading Core bus IDs, shows which core is a bus
            s = reader.readLine();
            s = s.replaceAll("\\{", "").replaceAll(",", "").replaceAll("\\}", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            while (tokenizer.hasMoreTokens()) {
                int index = Integer.valueOf(tokenizer.nextToken()).intValue();
                testCaseData.C[index].isBus = true;
            }

            reader.readLine();

            // reading Area of each cores
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.C[i].area = Double.valueOf(tokenizer.nextToken()).doubleValue();
            }

            reader.readLine();

            // reading Price of each cores
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.C[i].price = Double.valueOf(tokenizer.nextToken()).doubleValue();
            }

            reader.readLine();

            // reading Input number of each Core
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.C[i].n = Integer.valueOf(tokenizer.nextToken()).intValue();
            }

            reader.readLine();

            // reading Output number of each Core
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.C[i].m = Integer.valueOf(tokenizer.nextToken()).intValue();
            }

            for (int i = 0; i < 4; i++) {
                reader.readLine();
            }

            // reading a_ij, shows the possiblity of execution of tasks on cores
            for (int i = 0; i < testCaseData.n; i++) {
                s = reader.readLine();
                tokenizer = new StringTokenizer(s);
                for (int j = 0; tokenizer.hasMoreTokens(); j++) {
                    testCaseData.a[i][j] = Integer.valueOf(tokenizer.nextToken()).intValue();
                }
            }

            for (int i = 0; i < 5; i++) {
                reader.readLine();
            }

            // reading ex_ij, shows the execution time of a task when executed on a core
            for (int i = 0; i < testCaseData.n; i++) {
                s = reader.readLine();
                tokenizer = new StringTokenizer(s);
                for (int j = 0; tokenizer.hasMoreTokens(); j++) {
                    testCaseData.ex[i][j] = Double.valueOf(tokenizer.nextToken()).doubleValue();
                }
            }

            for (int i = 0; i < 5; i++) {
                reader.readLine();
            }

            // reading pw_ij, shows the power consumption of a task when executed on a core
            for (int i = 0; i < testCaseData.n; i++) {
                s = reader.readLine();
                tokenizer = new StringTokenizer(s);
                for (int j = 0; tokenizer.hasMoreTokens(); j++) {
                    testCaseData.pw[i][j] = Double.valueOf(tokenizer.nextToken()).doubleValue();
                }
            }

            reader.readLine();
            reader.readLine();

            // reading length of test busses
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.TB[i].testLength = Integer.valueOf(tokenizer.nextToken()).intValue();
            }

            reader.readLine();

            // reading area of test busses
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.TB[i].area = Double.valueOf(tokenizer.nextToken()).doubleValue();
            }

            reader.readLine();

            // reading scan length of each cores, for busses equals 0
            s = reader.readLine();
            s = s.replaceAll("\\[", "").replaceAll(",", "").replaceAll("\\]", "");
            tokenizer = new StringTokenizer(s);
            tokenizer.nextToken();
            for (int i = 0; tokenizer.hasMoreTokens(); i++) {
                testCaseData.C[i].t = Integer.valueOf(tokenizer.nextToken()).intValue();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return testCaseData;
    }

    private void buildPrecedenceRelationshipMatrix() {
        for (int i = 0; i < testCaseData.n; i++) {
            for (int j = 0; j < testCaseData.n; j++) {
                testCaseData.PR[i][j] = testCaseData.TG[i][j];
            }
        }
        for (int i = 0; i < testCaseData.n; i++) {
            for (int j = 0; j < testCaseData.n; j++) {
                for (int k = 0; k < testCaseData.n; k++) {
                    if (testCaseData.PR[i][k] == 1) {
                        for (int c = 0; c < testCaseData.n; c++) {
                            testCaseData.PR[i][c] |= testCaseData.PR[k][c];
                        }
                    }
                }
            }
        }
    }
}
