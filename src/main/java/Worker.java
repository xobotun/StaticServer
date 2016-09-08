import java.io.*;
import java.net.Socket;

public class Worker extends Thread {
    private final Socket client;
    private final String rootDir;

    private static final int DEFAULT_FILE_SIZE = 1024; // chars

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


            // TODO: if path is not correct 403
            final File file = new File(rootDir + request.getPath());
            if (file.exists() && file.isFile())
                out.print(AnswerMakerUtil.answerTemplate(ResponseCode.CODE_200, readFileExtension(file), readFile(file)));
            else
                out.print(AnswerMakerUtil.make404());

            out.close();
            in.close(); // Closes all connections somewhy, should be in end
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

    private String readFile(File file) throws FileNotFoundException, IOException {
        BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        final StringBuilder result = new StringBuilder(DEFAULT_FILE_SIZE);

        for (String line = fileStream.readLine(); line != null; line = fileStream.readLine())
            result.append(line).append("\r\n");

        fileStream.close();
        return result.toString();
    }

    private ContentType readFileExtension(File file) {
        final int dotPosition = file.getName().lastIndexOf('.');
        if (dotPosition == 0)
            return ContentType.TEXT;
        
        final String extension = file.getName().substring(dotPosition + 1);

        for (ContentType type : ContentType.types)
            if (type.getAliases().contains(extension.toLowerCase()))
                return type;

        return ContentType.TEXT;
    }
}
