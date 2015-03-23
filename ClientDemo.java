
import java.io.*;
import java.net.*;
 
public class ClientDemo {
	
	Socket socket = null;
	
	public void startClient(final String IPAddress, final int port, final Object obj) throws IOException {
		
		
		
		
		 Runnable clientTask = new Runnable() {
	            @Override
	            public void run() {
	                try {
	                	    ObjectOutputStream out = null;
	            			socket = new Socket(IPAddress, port);
	             
	            			out = new ObjectOutputStream(socket.getOutputStream());
	            			out.writeObject(obj);
	            			System.out.println("wrote to server"+obj+"ipadd and port"+IPAddress+port);
	            			
	            			out.flush();
	            			out.close();
	            			//socket.close();
	            			
	                    
	                } catch (Exception e) {
	                    System.err.println("Unable to process client request"+e.getMessage());
	                    e.printStackTrace();
	                }
	            }
	        };
	        Thread clientThread = new Thread(clientTask);
	        clientThread.start();

		
		
		
		
	}
}
