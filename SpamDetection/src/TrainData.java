import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TrainData extends JFrame
{
	JPanel panel;
	JButton next,submit;
	JRadioButton spam,nospam;
	JLabel checkin,badges,mayor,friends,tips,lists;
	static int userid=0;
	String[] users,p;
	
	public String readFile(String filename)
	{
		String content="";
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
			String line="";
			while((line = br.readLine()) != null)
				content += line;
			
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
	
	public String findParam(String userid)
	{
		try 
		{
			System.out.println("Function Called");
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
			System.out.println(checkin);
			
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
				System.out.println(tips);
			}
			else
			{
				tips="0";
				System.out.println(tips);
			}
			
			/* No of badges */
			
			String badges="";
			links = doc.select("div#userBadges");
			if(links.size() > 0)
			{
				for(Element link : links)
					badges += link.text();
				System.out.println("Error"+badges);
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
			System.out.println(badges);
			
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
			System.out.println(friends);
			
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
			System.out.println(mayor);
			
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
			System.out.println(lists);	
			String result = checkin + " " + tips+" "+friends+" "+mayor+" "+lists+" "+badges;
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	
	public String[] parseJSON()
	{
		JSONParser parser = new JSONParser();
		String text       = readFile("/home/hduser/Documents/refypscrapy/das_LA.json");
		Set<String> users = new TreeSet<String>();
		try 
		{
			JSONArray array   =  (JSONArray)parser.parse(text);
			
			System.out.println(array.get(1).toString());
			JSONObject ti    =  (JSONObject)array.get(1);
			JSONArray a       =  (JSONArray)parser.parse(ti.get("userName").toString());
			System.out.println(a.get(0).toString());
			
			for(int i=0;i<30;i++)
			{
				JSONObject tip = (JSONObject)array.get(i);
				JSONArray user = (JSONArray)parser.parse(tip.get("userName").toString());
				users.add(user.get(0).toString());
				//System.out.println(user.get(0).toString());
			}
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		String[] unique = users.toArray(new String[0]);
		return unique;
	}
	public TrainData() throws IOException
	{
		super("TrainData");
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		next = new JButton("Next");
		panel.add(next);
		
		users = parseJSON();
		
		FileOutputStream out = new FileOutputStream("/home/hduser/input/anotherInputFile");
		for(int i=0;i<users.length;i++)
		{
			out.write(users[i].getBytes());
			out.write(" ".getBytes());
		}
		
		String param = findParam(users[userid]);
		p = param.split(" ");
		checkin    = new JLabel("Check-ins " + p[0]);
		getContentPane().add(Box.createRigidArea(new Dimension(5, 0)));
		tips       = new JLabel("Tips " + p[1]);
		friends    = new JLabel("Friends " + p[2]);
		mayor      = new JLabel("Mayorships " + p[3]);
		lists      = new JLabel("Lists " + p[4]);
		badges     = new JLabel("Badges " + p[5]);
		spam       = new JRadioButton("Spam");
		nospam     = new JRadioButton("Legitimate");
		ButtonGroup group = new ButtonGroup();
		group.add(spam);
		group.add(nospam);
		
		panel.add(checkin);
		panel.add(tips);
		panel.add(friends);
		panel.add(mayor);
		panel.add(lists);
		panel.add(badges);
		panel.add(spam);
		panel.add(nospam);
		nospam.setSelected(true);
		
		submit = new JButton("Submit");

		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(userid < users.length)
				{
					String param = findParam(users[userid]);
					String[] p = param.split(" ");
					checkin.setText("Check-ins "+p[0]);
					tips.setText("Tips "+p[1]);
					friends.setText("Friends "+p[2]);
					mayor.setText("Mayorships "+p[3]);
					lists.setText("Lists "+p[4]);
					badges.setText("Badges "+p[5]);
					userid++;
				}
			}
		});
		getContentPane().add(panel);
		pack();
	}
}
