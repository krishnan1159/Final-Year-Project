import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Main 
{
	public Main()
	{
		
	}
	public String findUserId(String username)
	{
		String userId = null;
		Document doc;
		try
		{
			doc = Jsoup.connect("https://foursquare.com/"+username).get();
			String htmlContent = doc.html();
			Pattern regex   = Pattern.compile("(?<=\\/user\\/)\\d+(?=\\/)");
			Matcher matcher = regex.matcher(htmlContent);
			if(matcher.find())
				userId = matcher.group();
		}
		catch(IOException e)
		{
			System.out.println("IOException has occured");
		}
		return userId;
	}
	
	public List<String> findAllTips(String Url)
	{
		List<String> tips = null;
		
		String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.48 Safari/537.36";
		try
		{
			URL url = new URL(Url);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", userAgent);
			int responseCode = con.getResponseCode();
			System.out.println(responseCode);
			
			/* */
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
			String htmlContent=response.toString();
			//System.out.println(htmlContent);
			String re = "(?<=\"text\":\").*?(?=\",\"canonicalUrl)";
			Pattern regex   = Pattern.compile(re);
			Matcher matcher = regex.matcher(htmlContent);
			
			if(matcher.find())
			{
				tips = new ArrayList<String>();
				tips.add(matcher.group());
			}
			while(matcher.find())
				tips.add(matcher.group());
			
			return tips;
		}
		catch(IOException e)
		{
			System.out.println("An dd IOException has occured");
		}
		
		return tips;
	}
	
	public String formUrl(String userid)
	{
		String url = "https://api.foursquare.com/v2/lists/"+userid+"/tips?"
				+ "locale=en&v=20140328&id="+userid+"%2Ftips&limit=1000&"
				+ "sort=recent&offset=0&wsid=TXQMMUS0ZL5OMXN4TOEYVWTT5XVNEO&"
				+ "oauth_token=Z1X0XS5CU2N02NFDJM01TLVDOMC0AKUD1AA0GP2L5KE5RNGL";
		return url;
	}
	
	public static void main(String[] args)
	{
		/* Find each spam user belongs to what category */
		Main m = new Main();
		String userid = m.findUserId("tessa");
		
		/* Find all the tips posted by the user */
		String url = m.formUrl(userid);
		
		List<String> tips = m.findAllTips(url);
		
		System.out.println(userid);
	}
}
