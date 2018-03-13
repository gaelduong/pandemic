package api.socketcomm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Represents the server in the Client-Server communication model
 * @author Russell Katz
 */
public abstract class Server extends Thread {
    private ServerSocket server;
    private SendMessage sendMessage;

    public Server(int port) throws IOException {
        System.out.println("New server initialized!");
        this.server = new ServerSocket(port);
        this.sendMessage = new SendMessage();

        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = server.accept();
                final String data[] = client.getRemoteSocketAddress().toString().split(":");
                final String hostAddress = client.getInetAddress().getHostAddress();

                System.out.println(hostAddress + " (PORT:" + data[1] + ") connected");

                SocketBundle sb = new SocketBundle(client);
                sendMessage.add(sb);
                new MessageHandler(sb, this::handleReceivedMessage);
                onClientConnected(sb);

            } catch (SocketException e) {
                System.out.println("Server closed");
                break;

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * Call when done. ALWAYS!
     */
    public void close() {
        try {
            server.close();
        } catch (IOException ignore) {}
    }

    /**
     * @return # of clients connected (does not update if they disconnect!)
     * Recommended: keep track of client connected count in the server implementation
     */
    public int getClientCount() {
        return sendMessage.getConnectedClientCount();
    }

    public void sendMessageToClients(String msg, Object... objects) {
        sendMessage.writeMessageToClients(msg, objects);
    }

    public static boolean sendMessage(SocketBundle client, String msg, Object... objects) {
        return SendMessage.writeMessageToSocket(client, msg, objects);
    }

    protected abstract void handleReceivedMessage(SocketBundle client, List<Object> objects);

    protected abstract void onClientConnected(SocketBundle client);
}