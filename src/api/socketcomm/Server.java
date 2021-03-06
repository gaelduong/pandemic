package api.socketcomm;

import client.ClientCommands;
import pandemic.Game;
import pandemic.User;

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
    private Game game;

    public Server(Game g, int port) throws IOException {
        game = g;
        this.server = new ServerSocket(port);
        this.sendMessage = new SendMessage();

        this.start();
        System.out.println("New server initialized!");
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client = server.accept();
                final String data[] = client.getRemoteSocketAddress().toString().split(":");
                final String hostAddress = client.getInetAddress().getHostAddress();

                //String clientUsername = "client" + game.getGameManager().getActivePlayers().size();
                //User clientUser = new User(clientUsername, "ksjheukf", hostAddress);
                //game.getGameManager().joinGame(clientUser);


                System.out.println(hostAddress + " (PORT:" + data[1] + ") connected");

                SocketBundle sb = new SocketBundle(client);
                sendMessage.add(sb);

               // currentNumOfPlayerConnected++;
                //sendMessage(sb, ClientCommands.RECEIVE_NUM_OF_PLAYERS.name(), currentNumOfPlayerConnected);

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

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {return game;}

   // public int getCurrentNumOfPlayerConnected() {
   //     return currentNumOfPlayerConnected;
   // }


    protected abstract void handleReceivedMessage(SocketBundle client, List<Object> objects);

    protected abstract void onClientConnected(SocketBundle client);

}