import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        // Creating and initializing an object to read user entered input
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        // Creating and initializing a buffer object to read from the file
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // Constant conating the port number of FileServer
        final int serverSocket = 6969;

        // Constant containing the location of FileServer's directory for storing files
        final String fileDirectory = "D:\\Workspace\\IdeaProjects\\CCNProject\\File Client\\ClientFiles\\";

        // Variables for taking input from the user
        String uIPAddress = "";
        String uFilename = "";

        try {
            boolean validDataEntered = true;

            // This loop will validate the IPAddress entered by the use
            // It will ask the user to enter again, if the value is invalid
            do {
                validDataEntered = true;
                // Taking the value of IPAdress of the Server from the user
                System.out.print("Enter valid IP Address: ");
                uIPAddress = bufferedReader.readLine();
                if(!isValidIP(uIPAddress)) {
                    validDataEntered = false;
                    System.out.println("> Invalid IPAdress\n> Enter again!\n");
                }
            }
            while (!validDataEntered);

            // This loop will validate the Filename entered by the use
            // It will ask the user to enter again, if the value is invalid
            do {
                validDataEntered = true;
                // Taking the value of Filename from the user
                //uIPAddress = "127.0.0.1";
                System.out.print("Enter the File name: ");
                uFilename = bufferedReader.readLine();
                if(!isValidFilename(uFilename)) {
                    validDataEntered = false;
                    System.out.println("> File extension missing\n> Enter again\n");
                }
            }
            while (!validDataEntered);

            // Initializing a socket object to connect with the File Server
            Socket socket = new Socket(uIPAddress, serverSocket);

            // Initializing a PrintWriter object to write data to the server, through socket
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            // Writing/sending the filename entered by the user to the FileServer
            printWriter.println(uFilename);

            // Initizlizing an InputStream to read data from the FileServer, through scoket
            InputStream inputStream = socket.getInputStream();
            // Initizling an object to read the data sent from the FileServer
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            // Reading the serverCode sent by then FileServer
            int serverCode = bufferedInputStream.read();

            // Executes if the serverCode received from the FileServer is '0'
            if (serverCode == 1) {
                /* Initializing a BufferedOutputStream object to write data
                 * to the new file in the FileClient's directory for received files
                 */
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileDirectory + uFilename));
                // Creates an array for using as a buffer
                // Will be used for receiving data from the FileServer
                byte[] buffer = new byte[1024];

                // Variable for reading and evaluating the data read from the Socket
                int byteRead = 0;
                // Integer variable for counting the size of file in KiloBytes
                int fileSize = 0;

                System.out.println("\nSending the File...");
                System.out.print("Progress:   ");
                // The loop will keep executing as long as the FileClient is receving data from the FileClient
                while ((byteRead = bufferedInputStream.read(buffer)) >= 0) {
                    System.out.print(".");
                    // Writing data to the newly created file
                    bufferedOutputStream.write(buffer, 0 , byteRead);
                    // Calculating the file size in KiloBytes
                    fileSize++;
                }
                System.out.println();
                bufferedOutputStream.flush();
                System.out.println("File \'" + uFilename + "\' (" + (fileSize * 1000) + " Bytes) was successfully downloaded.");
            }
            // Executes if the serverCode received from the FileServer is '0'
            else if (serverCode == 0) {
                System.out.println("File \'" + uFilename + "\' is not present in the Server’s directory!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validate the the IPAddress entered by the User
    private static boolean isValidIP(String ipAddress){
        Pattern pattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.find();
    }

    // Validate the the Filename entered by the User
    private static boolean isValidFilename(String fileName) {
        //boolean containsExtension = true;
        int count = 0;
        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i) == '.') {
                count++;
            }
        }
        return (count == 1);
    }
}
