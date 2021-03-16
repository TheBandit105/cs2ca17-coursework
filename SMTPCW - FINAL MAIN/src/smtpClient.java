import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * This program demonstrates a SMTP Client protocol (based of the TCP Client class from Lab 2).
 * @author at015244
 * @version 1.0
 * @since 05-03-2021
 */

public class smtpClient {
    // The variables for the client side have been initialised
    private Socket serverSocket;
    private InetAddress serverAddress;
    private int serverPort;
    private Scanner scanner;

    /**
     * smtpClient
     * @param serverAddress
     * @param serverPort
     * @throws Exception
     */

    private smtpClient(InetAddress serverAddress, int serverPort) throws Exception {
        // The server's IP address and port number are assigned and set, based on the inputs given in the main method
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;

        // Initiate the connection with the server using Socket.
        // For this, creates a stream socket and connects it to the specified port number at the specified IP address.
        this.serverSocket = new Socket(this.serverAddress, this.serverPort);
        this.scanner = new Scanner(System.in);
    }

    /**
     * The clientCom method connect to the server and datagrams
     * The user can input messages to be sent to the server and
     * receive automated messages back from the server and print them.*
     * @throws IOException
     */

    private void clientCom() throws IOException {
        String d; // String variable to print server messages
        String in; // String variable to

        // New PrintStream constructor created from an existing OutputStream (i.e. serverSocket).
        // This convenience constructor creates the necessary intermediateOutputStreamWriter, which will convert characters into bytes using the default character encoding
        while (true) {
            in = scanner.nextLine(); // Allows user to input the messages of their choice
            PrintStream out = new PrintStream(serverSocket.getOutputStream(), true);
            out.println(in);

            if(in.equals("QUIT")){
                System.out.println("S: 221 gov.uk closing connection");
                serverSocket.close();
                break;
            }

            /* BufferedReader "listens" (in actuality, read) to the serverSocket to pick up on any message
             * that is transmitted from the server and if it detects any message waiting to be transmitted through the socket
             * this BufferReader will output the server's response since it has detected an incoming message.
             */
            BufferedReader i = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            out.flush();
            d = i.readLine();
            System.out.println(d);
        }
    }

    /**
     * The IP address and port number are set in the main method.
     * The client constructor is called and the given IP address and port number are passed
     * and the clientCom method is called afterwards.
     * IP address and port number are checked to make sure they're acceptable.
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        // Set the server address (IP) and port number
        InetAddress serverIP = InetAddress.getByName("192.168.56.1"); // local IP address
        int port = 7077;

        // Checks if IP address and port number are valid
        if (args.length > 0) {
            serverIP = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
        }

        // Call the constructor and pass the IP and port
        // Call clientCom method
        smtpClient client = new smtpClient(serverIP, port);
        System.out.println("\r\n Connected to Server: " + client.serverSocket.getInetAddress());
        System.out.println("\r\nS: 220 gov.uk");
        client.clientCom();
    }
}




