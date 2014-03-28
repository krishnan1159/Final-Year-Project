import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest 
{
	public static final String INDEX_DIR_PATH = "/home/hduser/prayaas/index";
	public static final String FILES_DIR_PATH = "/home/hduser/prayaas/input";
	private IndexWriter writer;
	
	
	public void readFile()
	{
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/hduser/prayaas/test.txt")));
			String line;
			while((line = br.readLine()) != null)
			{
				String[] fields = line.split("\t");
				for(int i=0;i<fields.length;i++)
					System.out.print(i+" "+fields[i]+"\t");
				System.out.println();
			}
			br.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initWriter() /* To Initialize IndexWriter class */
	{
		try 
		{
			Directory dir = FSDirectory.open(new File(INDEX_DIR_PATH));
			Analyzer a    = new StandardAnalyzer(Version.LUCENE_46);
			writer        = new IndexWriter(dir, new IndexWriterConfig(Version.LUCENE_46, a).setOpenMode(OpenMode.CREATE));	
		} 
		catch (IOException e) 
		{
			System.out.println("Error in Creating Index Writer. Please specify the folder Correctly for storing index");
			e.printStackTrace();
		}
	}
	
	public void indexFile(File file)
	{
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			while( (line=br.readLine()) != null)
			{
				String[] fields     = line.split("\t");
				String[] fieldNames = {"userid","usertype","age","gender","country","pva","sqa","minspent","visits"};
				Document doc = new Document();
				
				for(int i=0;i<5;i++)
					if(i != 2)
						doc.add(new StringField(fieldNames[i], fields[i],Store.YES));
					else
						doc.add(new IntField(fieldNames[i], Integer.parseInt(fields[i]),Store.YES));

				for(int i=7;i<=8;i++)
					doc.add(new IntField(fieldNames[i], Integer.parseInt(fields[i]),Store.YES));
				
				for(int i=5;i<7;i++)
					if(fields.length > 0)
					{
						String[] special = fields[i].split("\\|");
						
						for(int j=0;j<special.length;j++)
						{
							String[] temp = special[j].split(";");
							if(temp.length > 1)
							{
								doc.add(new StringField(fieldNames[i], temp[0],Store.YES));
								doc.add(new IntField(temp[0], Integer.parseInt(temp[1]),Store.YES));
							}
						}
					}
				List<IndexableField> test = doc.getFields();
				for(int i=0;i<test.size();i++)
				{
					System.out.print(test.get(i).toString()+" End ");
				}
				System.out.println();
				writer.addDocument(doc);

			}

			br.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void indexDirectory(String path) throws IOException
	{
		File dataDir = new File(path);
		File[] files = dataDir.listFiles();
		
		for(File file : files)
		{
			if( file.isDirectory() )
				indexDirectory(file.getAbsolutePath());
			else
				indexFile(file);
		}
		writer.commit();
		writer.close();
	}
	public void searchIndex()
	{
		//IndexReader reader = IndexReader.open(new File(INDEX_DIR_PATH));
	}
	
	public static void main(String[] args) throws IOException
	{
		LuceneTest lt = new LuceneTest();
		lt.initWriter();
		lt.indexDirectory(FILES_DIR_PATH);
		System.out.println("Success");
	}
}
