import java.net.ServerSocket;

public class Server {
    private ServerSocket socket;

    public Server(int port) throws Exception {
        try {
            socket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void run() {
        try {
            while (true) {
                final Worker worker = new Worker(socket.accept());
                worker.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
