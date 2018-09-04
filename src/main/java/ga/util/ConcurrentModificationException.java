package ga.util;

/**
 * Thrown by the iterator for SimpleList in the case of concurrent
 * modification.  A duplicate of JDK1.2 functionality, provided here
 * in order to simplify porting at a later date.
 */
public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException() {
    }

    public ConcurrentModificationException(String message) {
        super(message);
    }
}

