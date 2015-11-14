package test_client;
import java.io.IOException;  
import java.net.Socket;  
import java.net.UnknownHostException;  
  
public class Client {  
      
    public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {  
        final int length = 10;  
        String host = "localhost";  
        int port = 8000;  
          
        Socket[] sockets = new Socket[length];  
        for(int i = 0; i < length; i++) {  
            sockets[i] = new Socket(host, port);  
            System.out.println("the " + (i+1) + "connection is successful");  
        }  
          
        Thread.sleep(1500);  
        for(int i = 0; i < length; i++) {  
            sockets[i].close();  
        }  
    }  
}  
