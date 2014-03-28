package detection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class First {
	
  public static class TokenMapper 
       extends Mapper<Object, Text, Text, Text>{
	  
	  public String findParam(String userid)
		{
			try 
			{
				//System.out.println("Function Called");
				URL url1=new URL("https://www.foursquare.com"+userid);
				
				URLConnection conn =  url1.openConnection();
			    conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 ( .NET CLR 3.5.30729)");
			    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
			    String temp,str="";
			    while ((temp = in.readLine()) != null) 
			    {
	                  str = str + temp;
	            }
			    
				Document doc = Jsoup.parse(str);
				
				/* Extracting Check-ins */
				String checkin="";
				Elements links = doc.getElementsByTag("h4");
				if(links.size() > 0)
				{
					for(Element link : links)
					{
						checkin += link.text();
					}
				}
				else
					checkin="0";
				//System.out.println(checkin);
				
				/* No. of Tips */
				//System.out.println("Photos");
				String tips="";
				links = doc.select("div#tipsHeader");
				if(links.size() > 0)
				{
					for(Element link : links)
						tips += link.text();
					if(tips.split(" ").length > 2)
						tips = tips.split(" ")[2];
					else
						tips="0";
					//System.out.println(tips);
				}
				else
				{
					tips="0";
					//System.out.println(tips);
				}
				
				/* No of badges */
				
				String badges="";
				links = doc.select("div#userBadges");
				if(links.size() > 0)
				{
					for(Element link : links)
						badges += link.text();
					//System.out.println("Error"+badges);
					if(badges.split(" ").length > 3)
					{
						if(badges.split(" ")[0].equalsIgnoreCase("See"))
						{
							badges = badges.split(" ")[3];
							badges = badges.substring(1, badges.length()-1);
						}
						else
							badges="0";
					}
					else
						badges="0";
				}
				else
					badges="0";
				//System.out.println(badges);
				
				/* No of Friends */
				
				String friends="";
				links = doc.select("div#userFriends");
				if(links.size() > 0 )
				{
					for(Element link : links)
						friends += link.text();
					if(friends.split(" ").length > 3)
					{
						friends = friends.split(" ")[3];
						friends = friends.substring(1, friends.length()-1);
					}
					else
						friends = "0";
					
				}
				else
				{
					friends="0";
				}
				//System.out.println(friends);
				
				/* No of Mayors */
				
				String mayor="";
				links = doc.select("div#userMayorships");
				
				if(links.size() > 0)
				{
					for(Element link : links)
						mayor += link.text();
					if(mayor.split(" ").length > 2)
					{
						mayor = mayor.split(" ")[4];
						mayor = mayor.substring(1, mayor.length()-1);
					}
					else
						mayor = "0";
				}
				else
					mayor="0";
				//System.out.println(mayor);
				
				/* No of lists */
				
				String lists="";
				links = doc.select("div.listsHeader");
				
				if(links.size() > 0)
				{
					for(Element link : links)
						lists += link.text();
					if(lists.split(" ").length > 2)
						lists = lists.split(" ")[2];
					else
						lists = "0";
				}
				else
					lists="0";
				//System.out.println(lists);
				//System.out.println("checkin  "+checkin);
				checkin = checkin.replace(",", "");
				//System.out.println("Checking "+checkin.replace(",", ""));
				//System.out.println("checkin  "+checkin);
				tips = tips.replace(",", "");
				friends = friends.replace(",", "");
				mayor = mayor.replace(",", "");
				lists = lists.replace(",", "");
				badges = badges.replace(",", "");
				
				int nooftips = Integer.parseInt(tips);
				if(nooftips < 10)
				{
					System.out.println("The user has been discarded as he has not contributed much");
					return null;
				}
				
				String result = checkin + "," + tips+","+friends+","+mayor+","+lists+","+badges+",unknown";
				return result;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
			
		}
    //private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
	private Text attr = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        System.out.println(word.toString());
        String result = findParam(word.toString());
        
        if(result != null)
        {
        	attr.set(findParam(word.toString()));
        	context.write(word, attr);
        }
      }
    }
  }
  
  public static class IntSumReducer 
       extends Reducer<Text,Text,Text,Text> {
    

    public void reduce(Text key, Text values, 
                       Context context
                       ) throws IOException, InterruptedException {
      
    key.set("");
      context.write(key, values);
    }
  }


  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
   
    Job job = new Job(conf, "first");
    job.setJarByClass(First.class);
    job.setMapperClass(TokenMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
