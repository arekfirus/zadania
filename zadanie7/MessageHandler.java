package zadanie7;

import java.io.IOException;

public interface MessageHandler {
    void sendMessage(String message);
    void handleIncomingMessage(String message);
    void disconnect();

    void run() throws IOException;
}