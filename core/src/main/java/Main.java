
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    public static void main(String[] args) {
        //localhost 127.0.0.1
        //178.174.162.51

        ExecutorService executorService = Executors.newCachedThreadPool();

        try(ServerSocket socket = new ServerSocket(5050)) {
            while(true) {
                Socket client = socket.accept();

                executorService.submit(()-> handleConnection(client));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handleConnection(Socket client) {
        try {
            System.out.println(client.getInetAddress());
            System.out.println(Thread.currentThread().getName());
            var inputFromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));

            while (true) {
                var line = inputFromClient.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                System.out.println(line);
            }
            var outputToClient = new PrintWriter(client.getOutputStream());
            outputToClient.print("HTTP/1.1 404 Not Found!\r\n Content-length: 0\r\n\r\n");
            outputToClient.flush();
            inputFromClient.close();
            outputToClient.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
