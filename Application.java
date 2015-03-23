import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Map;
public class Application implements Serializable {
	public  int NodeID;
	public  int cs_max_request=0;
	public  int cs_request_delay=0;
	public  int cs_exec_duration=0;
	int i;
	public void readNodeCSDetails(int nodeId, String topologyFile)
	{
		BufferedReader br;
		NodeID=nodeId;
		try
		{
			br = new BufferedReader(new FileReader(topologyFile));
			String line = "";
			while((line=br.readLine())!=null)
			{
				String data[] = line.split(" ");
				//System.out.println("node is"+nodeId+"other is"+Integer.parseInt(data[0]));
				if(nodeId==Integer.parseInt(data[0]))
				{
					System.out.println("inside");
					cs_max_request=Integer.parseInt(data[2]);
					cs_request_delay=Integer.parseInt(data[3]);
					cs_exec_duration=Integer.parseInt(data[4]);
					break;									
				}				
			}
			br.close();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void call_cs_enter() throws InterruptedException
	{
		// execute according to read information
		readNodeCSDetails(Project2.CurrentNodeId, Project2.topology);
		System.out.println("max req is "+cs_max_request);
		for (i=0;i<cs_max_request;i++)
		{
			//long start_timestamp=System.currentTimeMillis();
			Algorithm.cs_enter();
			//check if flag is enabled 
			
			for (Map.Entry<Integer, String> entry : Algorithm.shared_keys.entrySet())
			{
				Integer key = entry.getKey();
				String value = entry.getValue();
				System.out.println( "Shared keys in application module"+ key + " => " + value);
			}
			
			cs_execute();
			/*long end_timestamp=System.currentTimeMillis();
			long diff = end_timestamp - start_timestamp;
			
			long diffSeconds = diff / 1000 % 60;
			// if the difference is less than cs_request_delay then sleep for the diff
			if (diffSeconds < cs_request_delay)
			{
				//sleep before making the second round
				Thread.sleep((cs_request_delay - diffSeconds)*1000);
				
			}
			else
			{
				//make the second cs request
			}*/
			Thread.sleep((cs_request_delay)*1000);
		}
		System.out.println("i is" +i);
		
	}
	
	public void cs_execute()
	{
		FileReadingWriting.CreateWriteFile(NodeID, "", "C:\\Users\\sharethramh\\aos_cs.txt", cs_exec_duration);
		Algorithm.cs_leave();
	}
	
	//public static void main(String []args)
	//{
		//Application ap=new Application();
		//ap.readNodeCSDetails(1, "topology.txt");
		//ap.cs_execute();
	//}
}
