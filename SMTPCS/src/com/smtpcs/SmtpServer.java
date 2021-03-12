package com.smtpcs;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SmtpServer {
    private ServerSocket server;
    private Socket ssend;
    private Scanner scanner;
    /**
     * The TCPServer constructor initiate the socket
     * @param ipAddress
     * @param port
     * @throws Exception
     */
    public SmtpServer(String ipAddress, int port) throws Exception {
        if (ipAddress != null && !ipAddress.isEmpty())
            this.server = new ServerSocket(port, 1, InetAddress.getByName(ipAddress));
        else
            this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
    }

    /**
     * The listen method listen to incoming client's datagrams and requests
     * @throws Exception
     */
    private void listener() throws IOException {
        // listen to incoming client's requests via the ServerSocket
        String data;
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();

        // to store transcript into a text file
        String fileName = "output.txt";
        final boolean append = true, autoflush = true;
        PrintStream printStream = new PrintStream(new FileOutputStream(fileName, append));
        System.out.println("\r\nS: 220 gov.uk");

        // print received datagrams from client
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while ((data = in.readLine()) != null) {

            printStream.println("\rC: " + data);
                if (data.startsWith("HELLO")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 " + data.toLowerCase() + ", pleased to meet you.");
                    printStream.println("\rS: 250 " + data.toLowerCase() + ", pleased to meet you.");
                } else if (data.startsWith("MAIL FROM: ")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 ok");
                    printStream.println("\rS: 250 ok");
                } else if (data.startsWith("RCPT TO: ")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 ok");
                    printStream.println("\rS: 250 ok");
                } else if (data.equals("DATA")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 354 End data with <CR><LF>.<CR><LF>");
                    printStream.println("\rS: 354 End data with <CR><LF>.<CR><LF>");
                } else if (data.equals(".")) {
                    System.out.println("\rC: " + data);
                    System.out.println("\rS: 250 ok - Message accepted for delivery");
                    printStream.println("\rS: 250 ok - Message accepted for delivery");
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

    private void sender() throws IOException{
        String csend;

        csend = scanner.nextLine();
        PrintWriter ret = new PrintWriter(this.ssend.getOutputStream(), true);
        ret.println(csend);
        ret.flush();

    }

    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }

    public int getPort() {
        return this.server.getLocalPort();
    }

    public static void main(String[] args) throws Exception {
        // set the server address (IP) and port number
        String serverIP = "127.0.0.1";
        int port = 7077;

        if (args.length > 0) {
            serverIP = args[0];
            port = Integer.parseInt(args[1]);
        }
        // call the constructor and pass the IP and port
        SmtpServer server = new SmtpServer(serverIP, port);
        System.out.println("\r\nRunning Server: " +
                "Host=" + server.getSocketAddress().getHostAddress() +
                " Port=" + server.getPort());
        server.listener();
        server.sender();
    }
}


