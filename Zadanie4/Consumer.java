package Zadanie4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Consumer {

        public static void main(String[] args) {
            try (Socket socket = new Socket("localhost", 4777)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    String message = reader.readLine();
                    System.out.println("Odebrano wiadomosc: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


