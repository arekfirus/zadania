package zadanie6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageServer {
    private List<MessageHandler> clients = new CopyOnWriteArrayList<>();
    private Queue<String> messageQueue = new ConcurrentLinkedQueue<>();

    private void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server nasłuchuje na porcie: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient połączony: " + clientSocket);

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String role = in.readLine(); // odczytaj rolę klienta

                boolean isPublisher = "PUBLISHER".equalsIgnoreCase(role);
                MessageHandler connectionHandler = new ConnectionHandler(clientSocket, this, isPublisher);
                clients.add(connectionHandler);

                Thread clientHandler = new Thread(connectionHandler);
                clientHandler.start();

                if (!isPublisher) {
                    processMessageQueue(connectionHandler);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, MessageHandler sender) {
        boolean hasConsumer = clients.stream().anyMatch(client -> !((ConnectionHandler) client).isPublisher());

        if (!hasConsumer) {
            messageQueue.add(message);
        } else {
            for (MessageHandler client : clients) {
                if (client != sender && !((ConnectionHandler) client).isPublisher()) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public synchronized void removeClient(MessageHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Klient został odłączony: " + clientHandler);
    }

    private void processMessageQueue(MessageHandler clientHandler) {
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll();
            if (message != null) {
                clientHandler.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        MessageServer server = new MessageServer();
        server.startServer(4772);
    }
}