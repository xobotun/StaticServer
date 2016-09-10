import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    private ServerSocket socket;
    private final String rootDir;
    private final Map<String, List<byte[]>> cache = new ConcurrentHashMap<String, List<byte[]>>();
    private final Queue<Socket> clientQueue = new ConcurrentLinkedQueue<Socket>();
    private final Thread[] workers;

    public Server(int port, String rootDir, int numThreads) throws Exception {
        this.rootDir = rootDir;

        try {
            socket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        workers = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++)
            workers[i] = new Worker(clientQueue, rootDir, cache);
    }

    public void run() {
        try {
            for (Thread worker : workers)
                worker.start();

            while (true) {
                clientQueue.add(socket.accept());

                for (int i = 0; i < workers.length; i++)
                    if (!workers[i].isAlive())
                        (workers[i] = new Worker(clientQueue, rootDir, cache)).start();
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
