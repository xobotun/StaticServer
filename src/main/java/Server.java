import java.net.ServerSocket;

public class Server {
    private ServerSocket socket;
    private final String rootDir;

    public Server(int port, String rootDir) throws Exception {
        this.rootDir = rootDir;
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
                final Worker worker = new Worker(socket.accept(), rootDir);
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
