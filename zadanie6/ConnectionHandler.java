package zadanie6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements MessageHandler {
    private Socket clientSocket;
    private MessageServer messageServer;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private boolean isPublisher;

    public ConnectionHandler(Socket clientSocket, MessageServer messageServer, boolean isPublisher) {
        this.clientSocket = clientSocket;
        this.messageServer = messageServer;
        this.isPublisher = isPublisher;

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
    public void handleIncomingMessage(String message) {
        System.out.println("Odebrano wiadomość od klienta: " + message);
        messageServer.broadcastMessage(message, this);
    }

    @Override
    public void disconnect() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = bufferedReader.readLine();
                if (message == null) {
                    break;
                }
                handleIncomingMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            messageServer.removeClient(this);
            disconnect();
        }
    }

    public boolean isPublisher() {
        return isPublisher;
    }
}