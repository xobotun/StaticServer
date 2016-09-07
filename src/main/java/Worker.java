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

            String response = "You have sent this request:<hr><pre>";
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isEmpty())
                    break;
                response += line;
            }

            out.print(AnswerMakerUtil.make200(response));

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
