import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker extends Thread {
    private final Socket client;

    public Worker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            final PrintWriter out = new PrintWriter(client.getOutputStream());

            out.print("HTTP/1.1 200 \r\n");
            out.print("Content-Type: text/plain\r\n");
            out.print("Connection: close\r\n");
            out.print("\r\n");

            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty())
                    break;
                out.print(line + "\r\n");
            }

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
