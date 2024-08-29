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
                System.out.println("httpserver in the while ");

                //accepts all incoming connections 
                Socket s = ss.accept();
              
                
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
            System.out.println("catch in httpserver");
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
    private FileInputStream fileStream;


    public HttpServerSession(HttpServer httpServer, Socket s){
        //socket the client uses 
        this.s = s;
        //reference to the main thread in the server
        this.httpServer = httpServer;
    }
    public void run() { // entry point into the HttpServerSession
		try {
            
            System.err.println(" bigg try ");
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            bos = new BufferedOutputStream(s.getOutputStream());
            HttpServerRequest httpServerRequest = new HttpServerRequest();
			

            //loop until HttpServerRequest:: is done()returns true
            while(!httpServerRequest.isDone()){
                
                String line = reader.readLine();

               
                //pass line into request 
                httpServerRequest.process(line);

                System.out.println("while loop ");
            }
            System.out.println("outside while");

            httpServerRequest.getFile();
            

            String hostname;

            if((hostname = httpServerRequest.getHost()) == null){
                hostname = "localhost:59876/" + httpServerRequest.getFile();
                System.out.println("small if ");
            }
            else{
                hostname += "/" + httpServerRequest.getFile();
                System.out.println("small else ");
            }


            File file = new File(hostname);

            if(file.exists() && !file.isDirectory()){

                System.out.println("if file exists or not directory ");
                
                println(bos, "HTTP/1.1 200 OK");
                println(bos, "");
                println(bos, "hello world");
                


                byte[] byteArray = new byte[59876];
                fileStream = new FileInputStream(file);
                int readByte;
                while((readByte = fileStream.read(byteArray)) != -1){
                    bos.write(byteArray, 0, readByte);
                }

            
                fileStream.close();
            }
            else{
                println(bos, "Http/1.1 200 OK");
                println(bos, "Http/1.1 200 OK");
                println(bos, "Http/1.1 200 OK");
                System.out.println("in to the else ");
            }
            s.close();
            bos.flush();

		} 
        
        catch (Exception e) {
			System.err.println("Exception: " + e);
            System.out.println("biggest catch");
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
