package zadanie7.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

final class ProducerConnectionHandler implements ConnectionHandler {

    private final Socket socket;
    private final BufferedReader bufferedReader;
    private final BlockingQueue<String> buffer;

    public ProducerConnectionHandler(Socket socket, BlockingQueue<String> buffer) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = bufferedReader.readLine();
                if (message == null) {
                    break;
                }
                buffer.put(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Disconnecting producer:" + socket);
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}