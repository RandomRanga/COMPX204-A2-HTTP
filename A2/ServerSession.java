import java.net.*;
import java.io.*;
import java.util.*;

class HttpServer
{
    public static void main(String args[])
    {   

        //the port we will run from. 
        int port = 59876;
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
                httpServer.start();
                
               
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
    private BufferedOutputStream bos;

    public HttpServerSession(HttpServer httpServer, Socket s){
        //socket the client uses 
        this.s = s;
        //reference to the main thread in the server
        this.httpServer = httpServer;
    }
    public void run() { // entry point into the HttpServerSession
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bos = new BufferedOutputStream(s.getOutputStream());
            HttpServerRequest httpServerRequest = new HttpServerRequest();
			
            //read a line of text from one client and send itto all other clients
            // String lines;
            // while ((lines = reader.readLine()) != null && !lines.isEmpty()){
            //     System.out.println(lines);
            // }
            // String lines = reader.readLine();
            // while(!lines.equals("") && lines != null){
            //     System.out.println(lines);
            //     lines = reader.readLine();
            // }

            //loop until HttpServerRequest:: is done()returns true
            while(!httpServerRequest.isDone()){
                String line = reader.readLine();
                //pass line into request 
                httpServerRequest.process(line);
            }

            httpServerRequest.getFile();
            httpServerRequest.getHost();


            
            println(bos, "HTTP/1.1 200 OK");
            println(bos, "");
            println(bos, "hello world");
            bos.flush();
            s.close();

		} 
        catch (Exception e) {
			System.err.println("Exception: " + e);
		}
        

    }


    private boolean println(BufferedOutputStream bos, String s)
    {
        String news = s + "\r\n";
        byte[] array = news.getBytes();
        try {
            bos.write(array, 0, array.length);
        } catch(IOException e) {
            return false;
        }
        return true;
    }



}

