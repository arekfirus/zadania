package zadanie4;

import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMessage implements Runnable {
    private Socket clientSocket;
    private MessageServer messageServer;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ClientMessage(Socket clientSocket, MessageServer messageServer) {
        this.clientSocket = clientSocket;
        this.messageServer = messageServer;

        try {
            this.bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        printWriter.println(message);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = bufferedReader.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Odebrano wiadomość od klienta: " + message);
                messageServer.broadcastMessage(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            messageServer.removeClient(this);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}