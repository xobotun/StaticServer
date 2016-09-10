
public class Main {
    private static final int DEFAULT_NUM_THREADS = 1;
    private static final int DEFAULT_PORT = 80;

    public static void main(String... args) throws Exception {
        final Arguments arguments;
        try {
            arguments = readArguments(args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Usage: Server.jar -r <rootDir> -p <port> -c <threadsNum>");
            return;
        }
        final Server server = new Server(arguments.getPort(), arguments.getRootDir(), arguments.getNumThreads());
        System.out.println("Server started at " + arguments.getPort() + " port. Serving from \"" + arguments.getRootDir() +
                "\" directory, running in " + arguments.getNumThreads() + " worker threads.");
        server.run();
    }

    private static class Arguments {
        private String rootDir;
        private int numThreads;
        private int port;

        public String getRootDir() {
            return rootDir;
        }

        public int getNumThreads() {
            return numThreads;
        }

        public int getPort() {
            return port;
        }

        public void setRootDir(String rootDir) {
            this.rootDir = rootDir;
        }

        public void setNumThreads(int numThreads) {
            this.numThreads = numThreads;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    private static Arguments readArguments(String... args) throws IllegalArgumentException {
        final Arguments arguments = new Arguments();
        arguments.setPort(DEFAULT_PORT);
        arguments.setNumThreads(DEFAULT_NUM_THREADS);
        
        try {
            if (args.length % 2 != 0 || args.length < 2)
                throw new IllegalArgumentException("Too few parameters specified!");

            for (int i = 0; i < args.length; i += 2) {
                if (args[i].equals("-r"))
                    arguments.setRootDir(args[i + 1]);
                else if (args[i].equals("-c"))
                    arguments.setNumThreads(Integer.parseInt(args[i + 1]));
                else if (args[i].equals("-p"))
                    arguments.setPort(Integer.parseInt(args[i + 1]));
                else throw new IllegalArgumentException("Unknown parameter \"" + args[i] + '\"');
            }
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(e);
        }

        return arguments;
    }
}
