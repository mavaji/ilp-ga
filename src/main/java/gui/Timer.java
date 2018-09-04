package gui;

import java.util.Date;
import java.util.Observable;

/**
 * This class is responsible to gain time passed since start of the
 * corresponding action and pass it to that action.
 *
 * @author Vahid Mavaji
 */
public class Timer extends Observable implements Runnable {
    /**
     * Initial time i.e. the start time the observer action.
     */
    private final long initialTime = new Date().getTime();

    public void run() {
        try {
            while (true) {
                setChanged();
                notifyObservers(new Long((new Date().getTime() - initialTime) / 1000));
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
