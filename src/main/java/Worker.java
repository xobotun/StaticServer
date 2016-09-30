import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Worker extends Thread {
    private final String rootDir;
    private final Map<String, List<byte[]>> cache;
    private final Queue<Socket> clientQueue;

    private static final int DEFAULT_FILE_SIZE = 1024; // chars

    public Worker(Queue<Socket> clientQueue, String rootDir, Map<String, List<byte[]>> cache) {
        this.rootDir = rootDir;
        this.cache = cache;
        this.clientQueue = clientQueue;
    }

    @Override
    public void run() {
        while (true) {
            Socket client = null;
            while (client == null)
                client = clientQueue.poll();

            try {
                final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                final CachingOutputStream cacher = new CachingOutputStream(client.getOutputStream(), cache);
                final PrintWriter out = new PrintWriter(cacher);

                final Request request;
                try {
                    request = new Request(in);
                } catch (IOException e) {
                    e.printStackTrace();
                    out.print(AnswerMakerUtil.make403());
                    out.close();
                    in.close();
                    client.close();
                    return;
                }

                if (!request.getMethod().toLowerCase().equals("get") && !request.getMethod().toLowerCase().equals("head")) {
                    out.print(AnswerMakerUtil.make405());
                    out.close();
                    in.close();
                    client.close();
                    return;
                }

                final String filename = request.getPath();
                if (!tryWritingFromCache(request.getMethod() + ' ' + getName(), cacher)) {
                    cacher.setFilename(filename);

                    if (!isFileInRootDir(filename))
                        out.print(AnswerMakerUtil.make403());

                    File file = new File(rootDir + filename);
                    if (file.exists() && file.isDirectory()) {
                        file = new File(rootDir + filename + "/index.html");
                        if (!file.exists()) {
                            out.print(AnswerMakerUtil.make403());
                            out.close();
                            in.close();
                            client.close();
                            return;
                        }
                    }

                    boolean isHead = request.getMethod().toLowerCase().equals("head");
                    if (file.exists() && file.isFile())
                            sendData(out, cacher, file, isHead);
                    else
                        out.print(AnswerMakerUtil.make404());
                }

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
    }

    private boolean tryWritingFromCache(String request, OutputStream client) throws IOException {
        if (!cache.containsKey(request))
            return false;

//        System.out.println("File \"" + filename + "\" found cached!");
        final List<byte[]> responses = cache.get(request);
        for (byte[] response : responses)
            client.write(response);

        return true;
    }

    private static void sendData(PrintWriter textOut, OutputStream binOut, File file, boolean isHead) throws IOException {
        final ContentType type = readFileExtension(file);

        textOut.print(AnswerMakerUtil.answerTemplate(ResponseCode.CODE_200, type, (int)file.length()));
        textOut.flush();

        if (!isHead) {
            final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            final byte[] binData = new byte[(int) file.length()];
            stream.read(binData);
            binOut.write(binData);
            binOut.flush();
        }
    }

    private static boolean isFileInRootDir(String filename) {
        int rootDirOffset = 0;

        final int length = filename.length();
        for (int i = 1; i < length - 1; i++)
            if (filename.charAt(i) == '/' && filename.charAt(i) != '/' /*|| filename.charAt(i) == '\\'*/) {
                if (length > i+2 && filename.charAt(i+1) == '.' && filename.charAt(i+2) == '.')
                    rootDirOffset--;
                else
                    rootDirOffset++;
                if (rootDirOffset < 0)
                    return false;
            }

        return true;
    }

    private static String readTextFile(File file) throws FileNotFoundException, IOException {
        BufferedReader fileStream = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        final StringBuilder result = new StringBuilder(DEFAULT_FILE_SIZE);

        for (String line = fileStream.readLine(); line != null; line = fileStream.readLine())
            result.append(line).append("\r\n");

        fileStream.close();
        return result.toString();
    }

    private static ContentType readFileExtension(File file) {
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
