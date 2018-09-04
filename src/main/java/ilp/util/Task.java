package ilp.util;

/**
 * This class represents a task in the ilp equations.
 *
 * @author Vahid Mavaji
 */
public class Task {
    /**
     * Flag indicating that this task is a communication task or not.
     */
    public boolean isCommunication = false;
    /**
     * Deadline of this task from start time of the first task,
     * if <code>-1</code> then task has no deadline.
     */
    public double deadLine = -1;
}
