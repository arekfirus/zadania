package zadanie7;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public class ProducerConnectionHandler extends ConnectionHandler implements Runnable {
    public ProducerConnectionHandler(Socket clientSocket, MessageServer messageServer, Queue<String> sharedQueue) {
        super(clientSocket, messageServer, true, sharedQueue);
    }

    @Override
    public void handleIncomingMessage(String message) {
        getMessageServer().broadcastMessage(message, this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = getBufferedReader().readLine();
                if (message == null) {
                    break;
                }
                handleIncomingMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getMessageServer().removeClient(this);
            disconnect();
        }
    }
}