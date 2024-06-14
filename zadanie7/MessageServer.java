package zadanie7;

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
    private Queue<String> sharedQueue = new ConcurrentLinkedQueue<>();

    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server nasłuchuje na porcie: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient połączony: " + clientSocket);

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String role = in.readLine(); // odczytaj rolę klienta

                MessageHandler connectionHandler;
                if ("PUBLISHER".equalsIgnoreCase(role)) {
                    connectionHandler = new ProducerConnectionHandler(clientSocket, this, sharedQueue);
                } else {
                    connectionHandler = new ConsumerConnectionHandler(clientSocket, this, sharedQueue) {
                        @Override
                        public void handleIncomingMessage(String message) {

                        }
                    };
                    processMessageQueue(connectionHandler);
                }

                clients.add(connectionHandler);
                Thread clientHandlerThread = new Thread((Runnable) connectionHandler);
                clientHandlerThread.start();
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

    public synchronized String getMessageFromQueue() {
        return messageQueue.poll();
    }

    public static void main(String[] args) {
        MessageServer server = new MessageServer();
        server.startServer(4774);
    }
}