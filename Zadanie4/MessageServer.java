package Zadanie4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MessageServer {
    private List<ClientMessage> clients = new ArrayList<>();

    private Queue<String> messageQueue = new LinkedList<>();

    public static void main(String[] args) {
        MessageServer server = new MessageServer();
        server.startServer(4777);
    }

    public void startServer(int port) {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server nasluchuje na porcie: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient polaczony: " + clientSocket);

                ClientMessage clientMessage = new ClientMessage(clientSocket, this);
                clients.add(clientMessage);

                Thread clientHandler = new Thread(clientMessage);
                clientHandler.start();

                processMessageQueue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(String message, ClientMessage sender) {
        if (clients.size() == 1 && clients.get(0) == sender) {
            messageQueue.add(message);
        } else {
            for (ClientMessage client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public void removeClient(ClientMessage clientMessage) {
        clients.remove(clientMessage);
        System.out.println("Klient zostal odlaczony: " + clientMessage.getClientSocket());
    }

    public synchronized void processMessageQueue() {
        while (!messageQueue.isEmpty()) {
            String message = messageQueue.poll();
            for (ClientMessage client : clients) {
                client.sendMessage(message);
            }
        }
    }
}


