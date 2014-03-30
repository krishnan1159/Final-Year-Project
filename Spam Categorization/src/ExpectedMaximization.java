import weka.clusterers.EM;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;


public class ExpectedMaximization 
{
	DataSource train,unknown,test;
	Instances trainData,unknownData;
	
	public ExpectedMaximization()
	{
		try
		{
			/* Loading the Data */
			train       = new DataSource("/home/hduser/emtest.arff");
			trainData   = train.getDataSet();
			trainData.setClassIndex(trainData.numAttributes()-1);
			
			/* Set Option for Random Forest Algorithm */
			String[] opt = Utils.splitOptions("-I 100 -S 5 -depth 0 -D");
			
			/* Training */
			EM em = new EM();
			em.setOptions(opt);
			
			/* Classify Users */
			unknown     = new DataSource("/home/hduser/fileTest1.arff");
			unknownData = unknown.getDataSet();
			unknownData.setClassIndex(unknownData.numAttributes()-1);
			
			Instances copy = new Instances(unknownData);
			
			for(int i=0;i<unknownData.numInstances();i++)
			{
				System.out.println("Hello ");
				double value = em.clusterInstance(unknownData.instance(i));
				copy.instance(i).setClassValue(value);
			}
			
			DataSink.write("/home/hduser/output1.arff", copy);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		ExpectedMaximization em = new ExpectedMaximization();
	}
}
