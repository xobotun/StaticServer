import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker extends Thread {
    private final Socket client;
    private final String rootDir;

    public Worker(Socket client, String rootDir) {
        this.client = client;
        this.rootDir = rootDir;
    }

    @Override
    public void run() {
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            final PrintWriter out = new PrintWriter(client.getOutputStream());

            final Request request;
            try {
                request = new Request(in);
            } catch (IOException e) {
                e.printStackTrace();
                out.print(AnswerMakerUtil.make403());
                return;
            }
            System.out.println(request.getPath());

            out.print(AnswerMakerUtil.make200(request.toString()));

            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Sum Ting Wong!");
            try {
                client.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
