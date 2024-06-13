package zadanie5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageServer {
    private List<MessageHandler> clients = new CopyOnWriteArrayList<>();

    private void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server nasłuchuje na porcie: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient połączony: " + clientSocket);

                MessageHandler connectionHandler = new ConnectionHandler(clientSocket, this);
                clients.add(connectionHandler);

                Thread clientHandler = new Thread((Runnable) connectionHandler);
                clientHandler.start();

                processMessageQueue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, MessageHandler sender) {
        for (MessageHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void removeClient(MessageHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Klient został odłączony: " + clientHandler.getClass().getSimpleName());
    }

    public synchronized void processMessageQueue() {
        // przetwarzanie kolejki wiadomości, jeśli potrzebne
    }

    public static void main(String[] args) {
        MessageServer server = new MessageServer();
        server.startServer(4777);
    }
}