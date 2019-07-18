package jdes.gdeltedu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

public class DataFrameInput {
	
	public static void main(String[] args) {
		String inputDir;
		String output;
		if (args.length == 2) {
			inputDir = args[0];
			output = args[1];
		} else {
			System.out.println("Error with commandline inputs!");
			return;
		}
		
		
		SparkSession spark = SparkSession.builder().master("local").appName("gdelt-education").
				config("some config", "value").getOrCreate();
		
		// creates an empty list to add the dataframes to
		List<Dataset<Row>> arrayOfDfs = new ArrayList<Dataset<Row>>();
		
		Pattern csvp = Pattern.compile(".*\\.csv");
		
		// test dir = /home/vmuser/testSparkFinal/
		// actual dir = /home/vmuser/stockdata/
		File[] folder = new File(inputDir).listFiles();
		for (File file : folder) {
				String filePath = file.getAbsolutePath();
				String inputFile = file.getName();
				Matcher m = csvp.matcher(inputFile);
					if (m.matches()) {
						createDataFrame(spark, filePath, inputFile, arrayOfDfs);
					}
				}
	
		
		
	}
	
	public static void createDataFrame(SparkSession spark, String filePath, String inputFile,
			List<Dataset<Row>> arrayOfDfs) {

		Dataset<Row> df = spark.read().format("csv").option("header", "true").load(filePath);


		//I add each array to an arraylist to be normalized later in the code
		arrayOfDfs.add(df);

}

}
	
	


