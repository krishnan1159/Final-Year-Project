
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;
public class RandomForest 
{
	/* Read the Data in ARFF format */
	DataSource train,unknown,test;
	Instances trainData,unknownData;
	public RandomForest()
	{
		try {
			/* Loading the Data */
			train       = new DataSource("/home/hduser/emtest.arff");
			trainData   = train.getDataSet();
			trainData.setClassIndex(trainData.numAttributes()-1);
			
			StringToNominal stm = new StringToNominal();
			stm.setAttributeRange("first");
			stm.setInputFormat(trainData);
			
			Instances afterFilter = Filter.useFilter(trainData, stm);
			
			/* Set Option for Random Forest Algorithm */
			String[] opt = Utils.splitOptions("-N 1 -D");
			
			/*Training */
			weka.classifiers.functions.LibSVM rf = new weka.classifiers.functions.LibSVM();
		
			//rf.setOptions(opt);
			//System.out.println(rf.getFilterType());
			
			rf.buildClassifier(afterFilter);
			
			/* Classify Users . A comment has been added*/
			unknown     = new DataSource("/home/hduser/fileTest1.arff");
			unknownData = unknown.getDataSet();
			unknownData.setClassIndex(unknownData.numAttributes()-1);
			
			Instances copy = new Instances(unknownData);
			
			for(int i=0;i<unknownData.numInstances();i++)
			{
				System.out.println("Hello ");
				double value = rf.classifyInstance(unknownData.instance(i));
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
		RandomForest r = new RandomForest();
	}
}
