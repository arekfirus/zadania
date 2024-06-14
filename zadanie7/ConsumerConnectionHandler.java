package zadanie7;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;

public abstract class ConsumerConnectionHandler extends ConnectionHandler implements Runnable {
    public ConsumerConnectionHandler(Socket clientSocket, MessageServer messageServer, Queue<String> sharedQueue) {
        super(clientSocket, messageServer, false, sharedQueue);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = getMessageServer().getMessageFromQueue();
                if (message == null) {
                    break;
                }
                sendMessage(message);
            }
        } finally {
            getMessageServer().removeClient(this);
            disconnect();
        }
    }
}
