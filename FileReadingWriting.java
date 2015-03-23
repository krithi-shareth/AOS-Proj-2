import java.io.*;
import java.util.Map;

public class FileReadingWriting implements Serializable {
	
   public static void CreateWriteFile(int nodeid, String content, String filename,int cs_exec_duration)
   {
	   
	      File f = null;
	      long start_timestamp=System.currentTimeMillis();
	      
	      content="\n"+content;
	     try
	      {
	         // create new file
	         f = new File(filename);
	         
	         // tries to create new file in the system
	         if(!f.exists())
	         {
	         }
	       
	         //System.out.println("File created: "+bool);
	                  
	        	FileWriter fw = new FileWriter(f.getAbsoluteFile(),true);
	 			BufferedWriter bw = new BufferedWriter(fw);
	 			bw.write("\nNode id:"+nodeid+" Start Timestamp:"+start_timestamp);
	 			String key_str="  ";
	 			for (Map.Entry<Integer, String> entry : Algorithm.shared_keys.entrySet())
				{
					Integer key = entry.getKey();
					String value = entry.getValue();
					System.out.println("in File application key ===" +key+ "values"+value);
					key_str = key_str.concat(key + " => " + value).concat("   ");
				}
	 			bw.write("keys="+key_str);
	 			Thread.sleep(cs_exec_duration*1000);
	 			long stop_timestamp=System.currentTimeMillis();
	 			bw.write("\nNode id:"+nodeid+" End Timestamp:"+stop_timestamp);
	 			bw.close();         
	      }
	      catch(Exception e)
	      {
	         e.printStackTrace();
	      }
   }
}
