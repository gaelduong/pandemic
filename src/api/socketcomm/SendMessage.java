package api.socketcomm;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles sending messages
 * @author Russell Katz
 */
public class SendMessage {
    private final List<SocketBundle> clients;

    public SendMessage(SocketBundle... c) {
        this.clients = Collections.synchronizedList(new ArrayList<>());
        Collections.addAll(clients, c);
    }

    public void add(SocketBundle client) {
        clients.add(client);
    }

    public void writeMessageToClients(String msg, Object... objects) {
        final List<SocketBundle> clientsToWrite = new ArrayList<>(clients);
        for (SocketBundle client : clientsToWrite) {
            writeMessageToSocket(client, msg, objects);
        }
    }

    public static boolean writeMessageToSocket(SocketBundle client, String msg, Object[] objects) {
        if (client.getSocket().isClosed()) return false;

        try {
            final List<Object> msgToSend = new ArrayList<>();
            msgToSend.add(msg);
            Collections.addAll(msgToSend, objects);

            return client.writeObject(msgToSend);
        } catch (SocketException ignore) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getConnectedClientCount() {
        return clients.size();
    }
}