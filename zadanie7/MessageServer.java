package zadanie7;

import zadanie7.connection.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageServer {
    private final BlockingQueue<String> buffer = new ArrayBlockingQueue<>(10);

    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Starting server on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                try {
                    new Thread(ConnectionHandler.createHandler(clientSocket, buffer)).start();
                } catch (IOException e) {
                    System.err.println("Error during setting connection with client: " + clientSocket);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MessageServer server = new MessageServer();
        server.startServer(4774);
    }
}