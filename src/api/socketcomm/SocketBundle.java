package api.socketcomm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Contains all information for sending and receiving information with a Socket
 * @author Russell Katz
 */
public class SocketBundle {

    private final Object oLock;
    private final Object iLock;

    private final Socket socket;
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public SocketBundle(Socket socket) throws IOException {
        this.socket = socket;
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.oos.flush();
        this.ois = new ObjectInputStream(socket.getInputStream());

        this.oLock = new Object();
        this.iLock = new Object();
    }

    public Socket getSocket() {
        return socket;
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        synchronized (iLock) {
            return ois.readObject();
        }
    }

    public boolean writeObject(Object messageToSend) throws IOException {
        synchronized (oLock) {
            oos.writeObject(messageToSend);
            oos.flush();
            oos.reset();
            return true;
        }
    }
}
