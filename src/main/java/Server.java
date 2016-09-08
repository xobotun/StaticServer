import java.net.ServerSocket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket socket;
    private final String rootDir;
    private final Map<String, List<byte[]>> cache = new ConcurrentHashMap<String, List<byte[]>>();

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
                final Worker worker = new Worker(socket.accept(), rootDir, cache);
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
