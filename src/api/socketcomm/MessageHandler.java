package api.socketcomm;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;

/**
 * Handles accepting received messages
 * @author Russell Katz
 */
public class MessageHandler extends Thread {
    protected SocketBundle client;

    private final LinkedBlockingQueue<List<Object>> messages;
    private BiConsumer<SocketBundle, List<Object>> handleMessage;

    public MessageHandler(SocketBundle client, BiConsumer<SocketBundle, List<Object>> handleMessage) {
        this.client = client;
        this.handleMessage = handleMessage;
        this.messages = new LinkedBlockingQueue<>();

        new Thread(this::handleMessages).start();   //executor
        this.start();                               //gatherer
    }

    @SuppressWarnings("unchecked")
    private void handleMessages() {
        while (true) {
            List<Object> objects;
            try {
                objects = (List<Object>) client.readObject();
                messages.put(objects);
            } catch (SocketException | EOFException ignore) {       //DCed
                break;
            } catch (IOException | InterruptedException e) {        //interrupt for queue#put, but should never happen, since no queue capacity
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e2) {                   //coded incorrectly, passed wrong params
                e2.printStackTrace();
            }
        }
    }

    public void run() {
        while (true) {
            try {
                List<Object> message = messages.take();
                handleMessage.accept(client, message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}