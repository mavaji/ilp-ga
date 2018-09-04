package communication;

import java.io.Serializable;

/**
 * This class represents an ID that has port and IP information of an island.
 *
 * @author Vahid Mavaji
 */
public class IslandID implements Serializable {
    /**
     * IP through which a message is sent or received.
     */
    public String IP;
    /**
     * Port through which a message is sent or received.
     */
    public int port;

    public IslandID(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IslandID)) {
            return false;
        } else {
            IslandID other = (IslandID) obj;
            return other.port == port && other.IP.equals(IP);
        }
    }
}
