import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class CachingOutputStream extends OutputStream{
    private OutputStream clientStream;
    private Map<String, List<byte[]>> cache;
    private String filename = null;

    public CachingOutputStream(OutputStream clientStream, Map<String, List<byte[]>> cache) {
        this.clientStream = clientStream;
        this.cache = cache;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public void write(int b) throws IOException {
        clientStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (filename != null)
            if(!cache.containsKey(filename)) {
                System.out.println("Caching file \"" + filename + "\"...");
                final List<byte[]> responses = new ArrayList<byte[]>();
                cache.put(filename, responses);
                responses.add(b.clone());
            } else
                cache.get(filename).add(b.clone());

        try {
            clientStream.write(b, off, len);
        } catch (SocketException e) {
            if (filename != null) {
                System.out.println("Error while sending file \"" + filename + "\". Removing cache.");
                cache.remove(filename);
            }
        }
    }

    @Override
    public void flush() throws IOException {
        clientStream.flush();
    }
}