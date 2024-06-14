package zadanie7;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Publisher {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4774);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            writer.println("PUBLISHER");

            System.out.println("Podaj wiadomość do publikacji: ");

            while (true) {
                String message = scanner.nextLine();
                writer.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}