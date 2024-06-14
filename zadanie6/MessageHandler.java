package zadanie6;

public interface MessageHandler extends Runnable {
    void sendMessage(String message);

    void handleIncomingMessage(String message);

    void disconnect();
}
