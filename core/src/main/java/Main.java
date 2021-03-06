
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    //Trådar
    //Tråd pool, ExecutorService
    //Atomic operations
    //Tråd säker, Thread safe
    //synchronized

    public static List<String> billboard = new ArrayList<>();

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(80)) {
            while (true) {
                Socket client = serverSocket.accept();
                executorService.submit(() -> handleConnection(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConnection(Socket client) {
        try {
            var inputFromClient = new BufferedReader(new InputStreamReader((client.getInputStream())));
            readRequest(inputFromClient);

            var outputToClient = new PrintWriter(client.getOutputStream());
            sendResponse(outputToClient);

            inputFromClient.close();
            outputToClient.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendResponse(PrintWriter outputToClient) {
        outputToClient.print("HTTP/1.1 404 Not Found\r\nContent-length: 0\r\n\r\n");
        synchronized (billboard) {
            for (String line : billboard) {
                outputToClient.print(line + "\r\n");
            }
        }
        outputToClient.print("\r\n");
        outputToClient.flush();
    }

    private static void readRequest(BufferedReader inputFromClient) throws IOException {
        List<String> tempList = new ArrayList<>();

        while (true) {
            var line = inputFromClient.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }
            tempList.add(line);
            System.out.println(line);
        }

        synchronized (billboard){
            billboard.addAll(tempList);
        }
    }
}







