package ilp.methods.multipopulation;

import communication.IslandID;
import communication.Message;
import communication.MessageReceiver;
import communication.MessageSender;
import ga.kit.Chromosome;
import ga.util.SortedList;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * This class represents an island that is particiapating in solving an ilp problem
 * with other islands in a distributed manner.
 *
 * @author Vahid Mavaji
 */
public class Island extends Observable implements Observer, Runnable {
    /**
     * Used to solve ilp equations of this island.
     */
    private ILPSolver ilpSolver = new ILPSolver();
    /**
     * Enqueues incoming immigrants and uses them in the next iterations.
     */
    private Vector immigrantsQueue = new Vector();
    /**
     * It is used to know the total state of the network and the state of this island in that network.
     */
    private Vector islandPool;
    /**
     * Indicates whether this island is conneted to the network or not.
     */
    private int state = ILPConstants.stNOT_CONNECTED;
    /**
     * Indicates that the computations are finished or not.
     */
    public boolean isFinished = false;
    /**
     * Is used for sending a message to the network.
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

    public Island() {
        messageReceiver = new MessageReceiver(ILPConstants.PORT);
        messageReceiver.addObserver(this);
        receiver = new Thread(messageReceiver);
        receiver.setDaemon(true);
        receiver.start();
    }

    public void release() {
        messageReceiver.release();
        receiver.stop();
    }

    public void execute() {

        // try to be added into the network...
        Message message = new Message(ILPConstants.tagADD_ME, ILPConstants.PORT);
        sender.send(message);
        setChanged();
        notifyObservers(message);

        try {
            while (!(state == ILPConstants.stCONNECTED)) {
                //waiting til be connected to the network.
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int iter = 0; iter < ILPConstants.ITERATIONS && !isFinished; iter++) {

            // insert new immigrant into the last population and remove the worst ones...
            SortedList orderedPopulation = new SortedList(ilpSolver.people);
            Chromosome[] oldPopulation = new Chromosome[orderedPopulation.size()];
            oldPopulation = (Chromosome[]) orderedPopulation.toArray(oldPopulation);

            for (int i = 0; !immigrantsQueue.isEmpty() && i < oldPopulation.length; i++) {
                oldPopulation[i] = (Chromosome) immigrantsQueue.remove(0);
            }

            ilpSolver.people.clear();
            for (int i = 0; i < oldPopulation.length; i++) {
                ilpSolver.people.add(oldPopulation[i]);
            }

            ilpSolver.solve();

            // selecting best indivisuals and prepare them for immigration.
            orderedPopulation = new SortedList(ilpSolver.people);
            oldPopulation = new Chromosome[orderedPopulation.size()];
            oldPopulation = (Chromosome[]) orderedPopulation.toArray(oldPopulation);
            int immigrant = (int) (oldPopulation.length * ILPConstants.MIGRATION_RATE);
            int totalSize = oldPopulation.length;
            Chromosome[] immigrants = new Chromosome[immigrant];
            for (int i = totalSize - immigrant; i < totalSize; i++) {
                immigrants[i - totalSize + immigrant] = oldPopulation[i];
            }

            message = new Message(immigrants, ILPConstants.tagBROADCAST_ME, ILPConstants.PORT);
            sender.send(message);
            setChanged();
            notifyObservers(message);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // finished its job and wants to be removed from the network...
        message = new Message(ILPConstants.tagREMOVE_ME, ILPConstants.PORT);
        sender.send(message);
        setChanged();
        notifyObservers(message);

        while (!(state == ILPConstants.stNOT_CONNECTED)) {
            //waiting til be unconnected from the network.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        IslandID islandid = (IslandID) islandPool.remove(0);
        islandPool.clear();
        islandPool.add(islandid);
        setChanged();
        notifyObservers(islandPool);

        release();
    }

    public void update(Observable o, Object obj) {
        if (o instanceof MessageReceiver && obj instanceof Message) {
            Message message = (Message) obj;
            setChanged();
            notifyObservers(message);
            if (message.getTag() == ILPConstants.tagISLAND_POOL) {
                islandPool = new Vector(message.getIslandPool());
                setChanged();
                notifyObservers(islandPool);
            } else if (message.getTag() == ILPConstants.tagACCEPTED) {
                state = ILPConstants.stCONNECTED;
            } else if (message.getTag() == ILPConstants.tagREMOVED) {
                state = ILPConstants.stNOT_CONNECTED;
            } else if (message.getTag() == ILPConstants.tagNEW_IMMIGRANTS) {
                // if some immigrants are to be entered in this island...
                Chromosome[] newImmigrants = new Chromosome[message.getSize()];
                newImmigrants = message.getChromosomes();
                for (int i = 0; i < newImmigrants.length; i++) {
                    immigrantsQueue.add(newImmigrants[i]);
                }
            }
        }
    }

    public void run() {
        execute();
    }
}
