package zadanie4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageServer {
    private List<ClientMessage> clients = new CopyOnWriteArrayList<>();

    private void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server nasłuchuje na porcie: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient połączony: " + clientSocket);

                ClientMessage clientMessage = new ClientMessage(clientSocket, this);

                synchronized (clients) {
                    clients.add(clientMessage);
                }

                Thread clientHandler = new Thread(clientMessage);
                clientHandler.start();

                processMessageQueue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, ClientMessage sender) {
        for (ClientMessage client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    public synchronized void removeClient(ClientMessage clientMessage) {
        clients.remove(clientMessage);
        System.out.println("Klient został odłączony: " + clientMessage.getClientSocket());
    }

    public synchronized void processMessageQueue() {
        // przetwarzanie kolejki wiadomości, jeśli potrzebne
    }

    public static void main(String[] args) {
        MessageServer server = new MessageServer();
        server.startServer(4778);
    }
}