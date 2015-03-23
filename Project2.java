import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

public class Project2 implements Serializable {
	
	public static TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
	public static TreeMap<Integer, String> origTreemap = new TreeMap<Integer, String>();
	public static int CurrentNodeId;
	static int server_port=0;
	static String topology="";
	static Socket socket1;
	public static void main(String[] args) throws Exception {
		
		// get details from file
		topology=args[1];
		CurrentNodeId = Integer.parseInt(args[0]);
		Algorithm al=new Algorithm();
		al.getNodeInfoFromFile(CurrentNodeId,topology );	
		server_port=Integer.parseInt(Algorithm.map.get(CurrentNodeId).split(":")[1]);
		//start the server in a separate thread
		
		Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                new Server(server_port);
            }
        };
        Thread serverThread = new Thread(serverTask);
        serverThread.start();
        
        // a delay if needed
        
		// check if all the servers are up and then call application module
        check_all_servers();
        
        Runnable cshandlerothertask = new Runnable() {
            @Override
            public void run() {
                Algorithm.cs_handler_other();
                System.out.println("Starting cs_handler_other thread");
            }
        };
        Thread handlerThread = new Thread(cshandlerothertask);
        handlerThread.start();
        
        // once it comes to this line it means all servers are up
        Application app = new Application();
		app.call_cs_enter();
		System.out.println("Calling cs_enter");
		System.out.println("application end");
        
		
	}// end of main
	
	public static void copyList()
	
	
	{
		for (Map.Entry<Integer, String> entry : Algorithm.map.entrySet())
		{
			treemap.put(entry.getKey(), entry.getValue());
			origTreemap.put(entry.getKey(), entry.getValue());
		}
	}
	
	public static void check_all_servers() throws IOException
	{
		copyList();
		while(true)
		{
			if (treemap.isEmpty())
			{
				//call the logic because all the servers are up and running 
				System.out.println("all servers up continuing....");
				//break out of the loop
				break;
			}
			
			for (Map.Entry<Integer, String> entry : origTreemap.entrySet())
		    {
				boolean remove_entry=false;
				System.out.println("key is "+entry.getKey());
				if (treemap.containsKey(entry.getKey()))
				{
					System.out.println(entry.getKey()+ "is present in treemap");
					//establish a socket
					String []nodeNetInfo=entry.getValue().split(":");
					try
					{
						System.out.println("sending socket to "+ nodeNetInfo[0]+ "  "+ nodeNetInfo[1]);
						socket1=new Socket(nodeNetInfo[0],Integer.parseInt(nodeNetInfo[1]));
						MessageStruct ms = new MessageStruct(9,CurrentNodeId,1,"");
						ObjectOutputStream out = null;
						out = new ObjectOutputStream(socket1.getOutputStream());
						out.writeObject(ms);
			           	out.flush();
			           	out.close();
						if (socket1.isConnected())
						{
							remove_entry = true;
							System.out.println("remove entry");
							//socket1.close();
						}
					}
					catch (Exception e)
					{
						//ignore exception
						System.out.println("exception is "+e.getMessage());
						//socket1.close();
						
					}
				}
				if (remove_entry)
				{
					System.out.println(entry.getKey() + "is connected and going to remove from treemap");
					treemap.remove(entry.getKey());
				}
		    }
			
		}
	}
	
} //end of class
