import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {



    public static void main(String[] args) {


            try {
                Socket socket = new Socket("localhost", 80);

                var output = new PrintWriter(socket.getOutputStream());

                output.println("Hello from client!\r\n\r\n");
                output.flush();


                var inputFromServer = new BufferedReader(new InputStreamReader((socket.getInputStream())));

                while (true) {

                    var line = inputFromServer.readLine();
                    if (line == null || line.isEmpty()) {
                        break;
                    }
                    System.out.print(line);
                }
                inputFromServer.close();
                output.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
