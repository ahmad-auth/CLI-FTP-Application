import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ClientThread extends Thread {

    // Private variable for creating a socket
    private Socket socket;

    // Private variables for performing I/O operations
    private BufferedReader bufferedReader;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;

    // This constructor assigns the socket received from the Main to the socket of this thread
    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    // Method for starting/running the thread
    @Override
    public void run() {

        try {
            // Initialize object for Reading the input from the FileClient system, through socket
            bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            // Initialize object for Writing the output to the socket, which sends to FileClient
            bufferedOutputStream = new BufferedOutputStream(this.socket.getOutputStream());

            // Constant containing the location of FileServer's directory for storing files
            final String fileDirectory = "D:\\Workspace\\IdeaProjects\\CCNProject\\File Server\\ServerFiles\\";

            // Read the input from the FileClient, through the socket
            String filename = bufferedReader.readLine();
            filename = filename.replace("\\n", "");

            System.out.println("File named " + filename + " has been requested by IPAddress \'" + socket.getLocalAddress() + "\'");

            // Creates and Initializes an object for performing i/o on the file
            File file = new File(fileDirectory + filename);

            // Check if the file exists
            if(file.exists()) {
                // Write data to the FileClient, through the socket
                // Writes '1' if file exist
                bufferedOutputStream.write(1);
                // Creates an array for using as a buffer
                // Will be used for sending the data to the FileClient
                byte[] buffer = new byte[1024];
                // Initializes the object for writing on the 'file' object
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

                // Variable for reading and evaluating the data read from the Socket
                int byteRead = 0;
                // The loop will keep executing as long as there is data to read in the file
                while ((byteRead = bufferedInputStream.read(buffer)) > 0) {
                    // Writing the data read from the File, to the FileClient
                    this.bufferedOutputStream.write(buffer, 0, byteRead);
                }
                this.bufferedOutputStream.flush();
            }
            else {
                // Write data to the FileClient, through the socket
                // Writes '0' if file does not exist
                bufferedOutputStream.write(0);
            }
        } catch (IOException e) {
            System.out.println("ClientThread has crashed!");
            System.exit(0);
            //e.printStackTrace();
        } finally {
            this.close();
        }
    }

    // Method for closing the thread
    public void close() {
        try {
            socket.close();
            bufferedReader.close();
            bufferedOutputStream.close();
        } catch (IOException e) {
            System.out.println("Error in closing I/O");
            System.exit(0);
            //e.printStackTrace();
        }
    }
}
