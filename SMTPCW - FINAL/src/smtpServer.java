import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This program demonstrates a SMTP server (based of the TCP Server class from Lab 2).
 * @author at015244
 * @version 1.0
 * @since 05-03-2021
 */

public class smtpServer {
    private ServerSocket server;

    /**
     * The TCPServer constructor initiate the socket
     * @param ipAddress
     * @param port
     * @throws Exception
     */

    public smtpServer(InetAddress ipAddress, int port) throws Exception {
        if (ipAddress != null){
            this.server = new ServerSocket(port, 1, InetAddress.getLocalHost());
        } else {
            this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
        }

    }

    /**
     * The serverCom method listen to incoming client's datagrams and requests
     * and returns a message back through the ServerSocket to the client, depending on
     * client's (user's) input.
     * @throws Exception
     */
    private void serverCom() throws IOException {
        // listen to incoming client's requests via the ServerSocket
        String data = null;
        Socket client = this.server.accept(); // Client's connection to the server is accepted

        /* Variable set in order to save the transcript between the server and client into a text file
        called "out.txt". The contents printed in the terminal are saved to this file.
         */
        String fileName = "out.txt";
        final boolean append = true, autoflush = true;
        PrintStream printStream = new PrintStream(new FileOutputStream(fileName, append));
        printStream.println("\r\nS: 220 gov.uk");

        // Setting the variable to send configured server messages back to the client.
        PrintStream out = new PrintStream(client.getOutputStream());

        // print received datagrams from client;
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while ((data = in.readLine()) != null) {

            /* The if else statements are used to decide which user inputs have been inputted and received
             by the server from the client side. For instance, when the client (user) inputs "HELLO" to the
             server, the client's message is printed out, as well as the server's predetermined message, which is "hello,
             pleased to meet you". The server's predetermined message is then also stored in the outputStream in order to
             return the message back to the client. printStream.println() stores said input to printStream, which gets
             inputted into out.txt. If "QUIT" is inputted by the client, the connection is terminated.
             */

            printStream.println("\rC: " + data);
                if (data.startsWith("HELLO")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 " + data.toLowerCase() + ", pleased to meet you.");
                    printStream.println("\rS: 250 " + data.toLowerCase() + ", pleased to meet you.");
                    out.println("S: " + data.toLowerCase() + ", pleased to meet you.");
                    out.flush();
                } else if (data.startsWith("MAIL FROM: ")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 ok");
                    printStream.println("\rS: 250 ok");
                    out.println("S: 250 ok");
                    out.flush();
                } else if (data.startsWith("RCPT TO: ")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 ok");
                    printStream.println("\rS: 250 ok");
                    out.println("S: 250 ok");
                    out.flush();
                } else if (data.equals("DATA")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 354 End data with <CR><LF>.<CR><LF>");
                    printStream.println("\rS: 354 End data with <CR><LF>.<CR><LF>");
                    out.println("S: 354 End data with <CR><LF>.<CR><LF>");
                    out.flush();
                } else if (data.endsWith(".")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 ok - Message accepted for delivery");
                    printStream.println("\rS: 250 ok - Message accepted for delivery");
                    out.println("S: 250 ok - Message accepted for delivery");
                    out.flush();
                } else if (data.startsWith("QUIT")){
                System.out.println("\rC: " + data);
                System.out.println("\rS: 221 gov.uk closing connection");
                printStream.println("\rS: 221 gov.uk closing connection");
                } else
                    System.out.println("\rC: " + data);
                    client.sendUrgentData(1);
                }
                System.setOut(printStream);
            }

    // Returns server IP
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    // Returns port number
    public int getPort() {
        return this.server.getLocalPort();
    }

    /**
     * The IP address and port number are set in the main method.
     * The server constructor is called and the given IP address and port number are passed
     * and the serverCom method is called afterwards.
     * IP address and port number are checked to make sure they're acceptable.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // set the server address (IP) and port number
        InetAddress serverIP = InetAddress.getByName("192.168.56.1");
        int port = 7077;

        // Checks if IP address and port number are valid
        if (args.length > 0) {
            serverIP = InetAddress.getByName(args[0]);
            port = Integer.parseInt(args[1]);
        }

        // Call the constructor and pass the IP and port
        // Call serverCom method
        smtpServer server = new smtpServer(serverIP, port);
        System.out.println("\r\nRunning Server: " +
                "Host=" + server.getSocketAddress().getHostAddress() +
                " Port=" + server.getPort());
        System.out.println("\r\nS: 220 gov.uk");
        server.serverCom();
    }
}


