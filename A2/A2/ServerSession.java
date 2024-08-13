import java.net.*;
import java.io.*;
import java.util.*;

class HttpServer
{
    public static void main(String args[])
    {   

        //the port we will run from. 
        int port = 51234;
        try{
            //creates a server socket to listen on the correct port and shows it. 
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            //keeps the server running 
            while(true){
                //accepts all incoming connections 
                Socket s = ss.accept();
                //creates a new writer to write to console
                PrintWriter write = new PrintWriter(s.getOutputStream(), true);

                //writes it to client
                System.out.println("Web Server Starting");


                InetAddress ia = s.getInetAddress();

             
                String ip = ia.getHostAddress();

             
                System.out.println("Your IP address is " + ip);


                //create a new httpserver object then should be able to print out the correct thing. 
                HttpServerSession httpServer = new HttpServerSession(null, s);
               
                
                s.close();
            }

        }
        //catches and prints out any errors
        catch (Exception e){
            System.err.println(e);
        }
    }
}


class HttpServerSession extends Thread {
    //each session gets a thread
    private HttpServer httpServer;
    private Socket s;
    private BufferedReader reader;
    private PrintWriter writer;

    public HttpServerSession(HttpServer httpServer, Socket s){
        //socket the client uses 
        this.s = s;
        //reference to the main thread in the server
        this.httpServer = httpServer;
    }
    public void run() { // entry point into the HttpServerSession
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
			
            // read a line of text from one client and send itto all other clients
            String in = reader.readLine();
            //httpServer.sayToAll(this, in);
            System.out.println(in);


			
		} 
        catch (Exception e) {
			System.err.println("Exception: " + e);
		}
    }

}
