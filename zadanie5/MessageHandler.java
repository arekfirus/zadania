package zadanie5;

public interface MessageHandler {
    void sendMessage(String message);
    void handleIncomingMessage(String message);
    void disconnect();
}