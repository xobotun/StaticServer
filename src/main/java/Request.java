import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private List<String> rawRequest = new ArrayList<String>();
    private String path;

    public Request(BufferedReader clientData) throws IOException {

        try {
            for (String line = clientData.readLine(); line != null && !line.isEmpty(); line = clientData.readLine())
                rawRequest.add(line);

            if (!rawRequest.isEmpty())
                path = readFilePath(rawRequest.get(0));
            else throw new IOException("Request is empty!");
        } catch (IOException e) {
            throw new IOException("This request seems corrupted:\n" + toString(), e);
        }
    }

    public List<String> getRawRequest() {
        return rawRequest;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(255);

        for (String line : rawRequest)
            builder.append(line).append("\r\n");

        return builder.toString();
    }

    public static String readFilePath(String firstLine) {
        final int firstSpace = firstLine.indexOf(' '); // GET <this one space> /adasd/asd%asd HTTP/1.1
        final int secondSpace = firstLine.indexOf(' ', firstSpace + 1); // GET /adasd/asd%asd <this one space> HTTP/1.1
        return firstLine.substring(firstSpace, secondSpace);
    }
}
