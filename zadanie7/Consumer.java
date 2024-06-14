package zadanie7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Consumer {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4774);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("CONSUMER");  // wysyłamy rolę

            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("Odebrano wiadomość: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}