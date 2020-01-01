import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Server...");
        ServerSocket serverSocket = null;
        boolean serverStatus;
        try {
            System.out.println("Server running...");
            // Initialize the serverSocket with the port number
            serverSocket = new ServerSocket(6969);
            // Varaible for checking the status of the server
            serverStatus = true;
            while (serverStatus) {
                Socket socket = serverSocket.accept();
                (new ClientThread(socket)).start();
            }
        } catch (IOException e) {
            serverStatus = false;
            System.out.println("Server has crashed!");
            System.exit(0);
            //e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
