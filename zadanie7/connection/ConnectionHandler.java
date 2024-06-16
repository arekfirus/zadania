package zadanie7.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public interface ConnectionHandler extends Runnable {

    static ConnectionHandler createHandler(Socket socket, BlockingQueue<String> buffer) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String role = in.readLine();

        if ("PUBLISHER".equalsIgnoreCase(role)) {
            System.out.println("Connecting producer: " + socket);
            return new ProducerConnectionHandler(socket, buffer);
        } else {
            System.out.println("Connecting consumer: " + socket);
            return new ConsumerConnectionHandler(socket, buffer);
        }
    }
}