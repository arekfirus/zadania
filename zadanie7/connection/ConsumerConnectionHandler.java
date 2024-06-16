package zadanie7.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

final class ConsumerConnectionHandler implements ConnectionHandler {

    private final Socket socket;
    private final PrintWriter printWriter;
    private final BlockingQueue<String> buffer;

    ConsumerConnectionHandler(Socket socket, BlockingQueue<String> buffer) throws IOException {
        this.socket = socket;
        this.printWriter = new PrintWriter(socket.getOutputStream(), true);
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = buffer.take();
                printWriter.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Disconnecting consumer:" + socket);
            try {
                printWriter.close();
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
