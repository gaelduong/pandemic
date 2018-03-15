package api.socketcomm;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Represents a client in the Client-Server communication model
 * @author Russell Katz
 */
public abstract class Client {

    protected SocketBundle client;

    public Client(String hostName, int port) throws IOException {
        this.client = new SocketBundle(new Socket(hostName, port));
        new MessageHandler(this.client, this::handleReceivedMessage);

    }

    public boolean sendMessageToServer(String command, Object... objects) {
        return SendMessage.writeMessageToSocket(client, command, objects);
    }

    public abstract void handleReceivedMessage(SocketBundle server, List<Object> message);

    /**
     * Call when done. ALWAYS!
     */
    public void close() {
        try {
            client.getSocket().close();
        } catch (IOException ignore) {}
    }

    public Socket getSocket() {
        return client.getSocket();
    }
}