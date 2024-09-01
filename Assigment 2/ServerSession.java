//Created by Toby Roberts 
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This class represents a simple HTTP server that listens on a specified port and processes incoming HTTP requests 
 * It creates a new thread for each incoming connection to handle the request asynchronously 
 */
class HttpServer {

    public static void main(String args[]) {

        // The port number on which the server will listen for incoming connections
        int port = 59876;

        try {
            // Create a server socket to listen on the specified port
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server is listening on port: " + port);

            // Main server loop, continuously accepts incoming connections and creates new threads when needed
            while (true) {

                // Accept an incoming connection from a client
                Socket s = ss.accept();

                // Create a new HttpServerSession thread to handle the request
                HttpServerSession httpServer = new HttpServerSession(null, s);
                httpServer.start();
            }
        } catch (Exception e) {
            // Catch and print any exceptions that occur during server operation
            System.err.println("Error: " + e.getMessage());
        }
    }
}


class HttpServerSession extends Thread {

    // Each session gets a thread
    private HttpServer httpServer;
    private Socket s;
    private BufferedReader reader;
    private BufferedOutputStream bos;
    private FileInputStream fileStream;


    public HttpServerSession(HttpServer httpServer, Socket s) {
        // Socket the client uses
        this.s = s;
        // Reference to the main thread in the server
        this.httpServer = httpServer;
    }

    // Entry point into the HttpServerSession
    public void run() { 

        try {
            // Create a BufferedReader to read from the client's input stream
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Create a BufferedOutputStream to write to the client's output stream
            bos = new BufferedOutputStream(s.getOutputStream());
            // Create a new HttpServerRequest object to parse the incoming HTTP request
            HttpServerRequest httpServerRequest = new HttpServerRequest();

            // Loop until the request is complete
            while (!httpServerRequest.isDone()) {

                // Read a line from the client's input stream
                String line = reader.readLine();

                // Process the line using the HttpServerRequest proccess object
                httpServerRequest.process(line);
            }

            // Get the hostname and filename from the request
            String hostname = httpServerRequest.getHost();
            File filename = new File(httpServerRequest.getFile());

            // Construct the full file path based on the hostname and filename. Although we don't use, it can be helpful. 
            String filePath = hostname + "/" + filename;

            // Check if the file exists and is not a directory
            if (filename.exists() && !filename.isDirectory()) {

                // Send a 200 OK response to the client, indicating the file was found.
                println(bos, "HTTP/1.1 200 OK");
                println(bos, "");

                // Read the contents of the file and send them to the client
                byte[] byteArray = new byte[5000];
                fileStream = new FileInputStream(filename);
                int readByte;
                while ((readByte = fileStream.read(byteArray)) != -1) {
                    bos.write(byteArray, 0, readByte);
                    // Slows the program down, can comment out if not wanted
                    Thread.sleep(1000);
                }

                fileStream.close();
            } else {
                // Send a Not Found response to the client, indicating the file was not found.
                println(bos, "HTTP/1.1 404 Not Found");
                println(bos, "");
            }

            // Flush the output stream 
            bos.flush();
            // Close the socket.
            s.close();

        } catch (Exception e) {
            // Catch and print any exceptions that occur during request handling.
            System.err.println("Error: " + e.getMessage());
        }
    }


    private boolean println(BufferedOutputStream bos, String s) {
        // Write a line to the output stream, including a newline character.
        String news = s + "\r\n";
        byte[] array = news.getBytes();
        try {
            bos.write(array, 0, array.length);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}