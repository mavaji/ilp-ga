package communication;

import ga.kit.Chromosome;
import ilp.methods.multipopulation.ILPConstants;

import java.io.Serializable;
import java.util.Vector;

/**
 * This class represents a message that can be moved between nodes of the network.
 *
 * @author Vahid Mavaji
 */
public class Message implements Serializable {
    /**
     * Chromosomes of this message.
     */
    private Chromosome[] chromosomes;
    /**
     * Size of the chromosomes this message has.
     */
    private int size;
    /**
     * Current set of all of nodes in the network that can be moved between island.
     */
    private Vector islandPool = new Vector();
    /**
     * Tag of the message that indicates what kind of information is contained in this message.
     */
    private int tag;
    /**
     * Sender's port of the message.
     */
    private int sourcePort;
    /**
     * Sender's IP of the message.
     */
    private String sourceIP = ILPConstants.IP;
    /**
     * Receiver's port of the message.
     */
    private int destPort = ILPConstants.BOOT_STRAP_PORT;
    /**
     * Receiver's IP of the message.
     */
    private String destIP = ILPConstants.BOOT_STRAP_IP;

    public Message(Vector islandPool, int tag, int sourcePort) {
        this.tag = tag;
        this.islandPool = new Vector(islandPool);
        this.sourcePort = sourcePort;
    }

    public Message(Vector islandPool, int tag) {
        this.tag = tag;
        this.islandPool = new Vector(islandPool);
        this.sourcePort = ILPConstants.BOOT_STRAP_PORT;
        this.sourceIP = ILPConstants.BOOT_STRAP_IP;
    }

    public Message(Chromosome[] chromosomes, int tag, int sourcePort) {
        this.tag = tag;
        this.size = chromosomes.length;
        this.chromosomes = new Chromosome[size];
        System.arraycopy(chromosomes, 0, this.chromosomes, 0, size);
        this.sourcePort = sourcePort;
    }

    public Message(Chromosome[] chromosomes, int tag) {
        this.tag = tag;
        this.size = chromosomes.length;
        this.chromosomes = new Chromosome[size];
        System.arraycopy(chromosomes, 0, this.chromosomes, 0, size);
        this.sourcePort = ILPConstants.BOOT_STRAP_PORT;
        this.sourceIP = ILPConstants.BOOT_STRAP_IP;
    }

    public Message(int tag, int sourcePort) {
        this.tag = tag;
        this.size = 0;
        this.chromosomes = null;
        this.islandPool = null;
        this.sourcePort = sourcePort;
    }

    public Message(int tag) {
        this.tag = tag;
        this.size = 0;
        this.chromosomes = null;
        this.islandPool = null;
        this.sourcePort = ILPConstants.BOOT_STRAP_PORT;
        this.sourceIP = ILPConstants.BOOT_STRAP_IP;
    }

    public int getSize() {
        return size;
    }

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    public int getTag() {
        return tag;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public void setDestIP(String destIP) {
        this.destIP = destIP;
    }

    public int getDestPort() {
        return destPort;
    }

    public String getDestIP() {
        return destIP;
    }

    public Vector getIslandPool() {
        return islandPool;
    }
}
