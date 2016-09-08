
public class Main {
    public static void main(String[] args) throws Exception {
        final Server server = new Server(80, "A:/www");
        server.run();
    }
}
