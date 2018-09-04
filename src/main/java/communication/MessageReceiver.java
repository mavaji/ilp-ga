package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;

/**
 * This class represents a receiver thread that listens continuously to it's port
 * and delivers received messages to it's corresponding entity.
 *
 * @author Vahid Mavaji
 */
public class MessageReceiver extends Observable implements Runnable {
    /**
     * The port that listening is done through it.
     */
    private ServerSocket receiver;
    private Socket socket = new Socket();

    public MessageReceiver(int port) {
        try {
            receiver = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (!receiver.isClosed()) {
                socket = receiver.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) inputStream.readObject();
                setChanged();
                notifyObservers(message);

                socket.close();
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            socket.close();
            receiver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
