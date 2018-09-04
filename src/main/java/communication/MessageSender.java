package communication;

import ilp.methods.multipopulation.ILPConstants;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class represents a sender that can be used to send a message to the network.
 *
 * @author Vahid Mavaji
 */
public class MessageSender {

    public void send(Message message, String IP, int port) {
        try {
            Socket socket = new Socket(IP, port);
            send(message, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) {
        try {
            Socket socket = new Socket(ILPConstants.BOOT_STRAP_IP, ILPConstants.BOOT_STRAP_PORT);
            send(message, socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Message message, Socket socket) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(message);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
