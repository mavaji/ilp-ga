package ilp.methods.multipopulation;

import communication.IslandID;
import communication.Message;
import communication.MessageReceiver;
import communication.MessageSender;
import ga.kit.Chromosome;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * This class represents a boot strap node in a network of islands.
 * It is responsible for adding, removing an island or transmit messages between island.
 * We can have redundancy i.e. have more than one boot strap in a network to obtain dependability.
 *
 * @author Vahid Mavaji
 */
public class BootStrap extends Observable implements Observer {
    /**
     * Is used do ilp equation related tasks.
     */
    private ILPSolver ilpSolver = new ILPSolver();
    /**
     * ID of the boot strap node.
     */
    private IslandID myID = new IslandID(ILPConstants.BOOT_STRAP_IP, ILPConstants.BOOT_STRAP_PORT);
    /**
     * Set of all of nodes (boot straps and islands) in the network.
     */
    private Vector islandPool = new Vector();
    /**
     * Contains the last message of each island, its a hash map that keys are island ids.
     */
    private HashMap messagePool = new HashMap();
    /**
     * Port from which an island has sent a message.
     */
    private int islandPort = 0;
    /**
     * IP address of the island that has sent a message.
     */
    private String islandIP;
    /**
     * ID (port and IP) of the island that has sent a message.
     */
    private IslandID islandID;
    /**
     * Last message received from an island.
     */
    private Message message;
    /**
     * Indicates that the computations are finished or not.
     */
    public boolean isFinished = false;
    /**
     * Number of unsatisfied constraints.
     */
    private int unsatisfied = -1;
    /**
     * Sends messages to the network.
     */
    private MessageSender sender = new MessageSender();
    /**
     * Is used to receive a message from the network.
     */
    MessageReceiver messageReceiver;
    /**
     * It is a thread that decorates the messageReceiver.
     */
    private Thread receiver;

    public BootStrap() {
        messageReceiver = new MessageReceiver(ILPConstants.BOOT_STRAP_PORT);
        messageReceiver.addObserver(this);
        receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        islandPool.add(myID);
    }

    public void release() {
        messageReceiver.release();
        receiver.stop();
    }

    public void execute() {
        setChanged();
        notifyObservers(islandPool);
        Chromosome[] chromosomes = new Chromosome[ilpSolver.people.size()];
        Iterator iter = ilpSolver.people.iterator();
        for (int i = 0; iter.hasNext(); i++) {
            chromosomes[i] = (Chromosome) iter.next();
        }
        evaluateResult(chromosomes);

        receiver.start();
    }

    public void update(Observable o, Object obj) {
        if (isFinished && islandPool.size() == 1) {
            endOfComputation();
        }

        if (o instanceof MessageReceiver && obj instanceof Message) {
            message = (Message) obj;
            setChanged();
            notifyObservers(message);
            islandPort = message.getSourcePort();
            islandIP = message.getSourceIP();
            islandID = new IslandID(islandIP, islandPort);

            if (message.getTag() == ILPConstants.tagADD_ME) {
                if (isFinished) {
                    StringBuffer note = new StringBuffer();
                    note.append("It is going to finish computation, no furthur request is accepted\n");
                    setChanged();
                    notifyObservers(note);
                } else {
                    islandPool.add(new IslandID(islandIP, islandPort));
                    setChanged();
                    notifyObservers(islandPool);
                    sendIslandPool(islandPool);

                    Message msg = new Message(ILPConstants.tagACCEPTED);
                    msg.setDestPort(islandPort);
                    msg.setDestIP(islandIP);
                    sender.send(msg, islandIP, islandPort);
                    setChanged();
                    notifyObservers(msg);
                }
            } else if (message.getTag() == ILPConstants.tagREMOVE_ME) {
                if (islandPool.size() != 1) {
                    Vector pool = new Vector(islandPool);
                    islandPool.remove(new IslandID(islandIP, islandPort));
                    setChanged();
                    notifyObservers(islandPool);
                    sendIslandPool(pool);

                    Message msg = new Message(ILPConstants.tagREMOVED);
                    msg.setDestPort(islandPort);
                    msg.setDestIP(islandIP);
                    sender.send(msg, islandIP, islandPort);
                    setChanged();
                    notifyObservers(msg);
                }
                if (islandPool.size() == 1 && isFinished) {
                    endOfComputation();
                }
            } else if (message.getTag() == ILPConstants.tagBROADCAST_ME) {
                messagePool.put(islandID, message);
                broadcast();
            }
        }
    }

    public void sendIslandPool(Vector pool) {
        IslandID id;
        for (Iterator iter = pool.iterator(); iter.hasNext();) {
            id = (IslandID) iter.next();
            if (!id.equals(myID)) {
                Message msg = new Message(islandPool, ILPConstants.tagISLAND_POOL);
                msg.setDestPort(id.port);
                msg.setDestIP(id.IP);
                sender.send(msg, id.IP, id.port);
            }
        }
    }

    private void broadcast() {
        IslandID id;
        for (Iterator iter = islandPool.iterator(); iter.hasNext();) {
            id = (IslandID) iter.next();
            if (!id.equals(islandID) && !id.equals(myID)) {
                Message msg = new Message(message.getChromosomes(), ILPConstants.tagNEW_IMMIGRANTS);
                msg.setDestPort(id.port);
                msg.setDestIP(id.IP);
                sender.send(msg, id.IP, id.port);
                setChanged();
                notifyObservers(msg);
            }
        }
    }

    private void endOfComputation() {
        release();
        if (messagePool.size() == 0) {
            return;
        }
        int totalSize = 0;
        Iterator iter = messagePool.values().iterator();
        while (iter.hasNext()) {
            totalSize += ((Message) iter.next()).getSize();
        }
        Chromosome[] chromosomes = new Chromosome[totalSize];

        iter = messagePool.values().iterator();
        int index = 0;
        while (iter.hasNext()) {
            Message msg = (Message) iter.next();
            int j = msg.getSize();
            Chromosome[] temp = new Chromosome[j];
            temp = msg.getChromosomes();
            for (int i = index; i < index + j; i++) {
                chromosomes[i] = temp[i - index];
            }
            index += j;
        }

        evaluateResult(chromosomes);
    }

    private void evaluateResult(Chromosome[] chromosomes) {
        try {
            Chromosome maxChrom = new Chromosome();
            Chromosome chrom = new Chromosome();
            double max = -Double.MAX_VALUE;
            double curval = 0;

            for (int i = 0; i < chromosomes.length; i++) {
                chrom = chromosomes[i];
                curval = chrom.getFitnessValue();
                if (curval > max) {
                    max = curval;
                    maxChrom = chrom;
                }
            }

            ilpSolver.computeFitness(maxChrom);

            StringBuffer result = new StringBuffer();
            result.append("\n");
            result.append("The minimum value for file: " + ILPConstants.TEST_CASE_FILE + " is: " + "\n");
            result.append("Fitness of the best chromosome is: " + maxChrom.getFitnessValue() + "\n");
            result.append("test time = " + ilpSolver.testTime + "\n");
            result.append("Area = " + ilpSolver.AREA + "\n");
            result.append("Power = " + ilpSolver.POWER + "\n");

            int unsatisfied = 0;
            for (int i = 0; i < ILPConstants.NUM_CONSTRAINTS; i++) {
                result.append(ilpSolver.c[i] / ILPConstants.PENALTIES[i] + " ");
                unsatisfied += ilpSolver.c[i] / ILPConstants.PENALTIES[i];
            }

            result.append("\nThere are " + unsatisfied + " unsatisfied constraints.\n");
            if (this.unsatisfied != -1) {
                result.append((float) (this.unsatisfied - unsatisfied) / (float) this.unsatisfied * 100
                        + " percent of constraints have been removed.\n");
            }
            this.unsatisfied = unsatisfied;

            result.append("\n");

            setChanged();
            notifyObservers(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) {
        BootStrap bootStrap = new BootStrap();
        try {
            while (!bootStrap.isFinished) {
                Thread.sleep(300);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
