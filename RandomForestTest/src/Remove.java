import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Remove 
{
	public static String readFile(String filename)
	{
		String content="";
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			FileOutputStream out = new FileOutputStream("/home/hduser/fileTest1");
			String line="";
			while((line = br.readLine()) != null)
			{
				content = line;
				out.write(content.split("\t")[1].getBytes());
				out.write("\n".getBytes());
			}
			
			br.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public static void main(String[] args)
	{
		String filename="/home/hduser/output11/part-r-00000";
		String content=readFile(filename);
		
	}
}
