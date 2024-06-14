package zadanie7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;

public abstract class ConnectionHandler implements MessageHandler {
    private Socket clientSocket;
    private MessageServer messageServer;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private boolean isPublisher;
    private Queue<String> sharedQueue;

    public ConnectionHandler(Socket clientSocket, MessageServer messageServer, boolean isPublisher, Queue<String> sharedQueue) {
        this.clientSocket = clientSocket;
        this.messageServer = messageServer;
        this.isPublisher = isPublisher;
        this.sharedQueue = sharedQueue;

        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        printWriter.println(message);
    }

    @Override
    public void disconnect() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPublisher() {
        return isPublisher;
    }

    public MessageServer getMessageServer() {
        return messageServer;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public Queue<String> getSharedQueue() {
        return sharedQueue;
    }
}