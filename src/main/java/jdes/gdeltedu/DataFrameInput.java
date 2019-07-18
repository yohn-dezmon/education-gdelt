package jdes.gdeltedu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
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
		
		for (Dataset<Row> dataframe : arrayOfDfs) {
			// this creates a table nasdaq that I can run sql queries on
			dataframe.createOrReplaceTempView("gdeltedu");
			
			// generating dataset for query by states...
			Dataset<Row> filterByState = dataframe.sqlContext().sql("Select GLOBALEVENTID, FractionDate, Actor1Code,"
					+ " Actor1Name, AvgTone, EventCode,"
					+ " ActionGeo_ADM1Code, ActionGeo_FullName, NumMentions, SOURCEURL from gdeltedu"
					+ " where ActionGeo_ADM1Code = 'USTX'");
			
			filterByState.show();
			
			

			
			// local output: /home/vmuser/stockdata/joineddata/nasdaq
			// this saves individual parquet files into a folder, that can later be accessed as one dataframe
//			filtered.write().mode(SaveMode.Append).parquet(output);

		}
	
		
		
	}
	
	public static void createDataFrame(SparkSession spark, String filePath, String inputFile,
			List<Dataset<Row>> arrayOfDfs) {

		Dataset<Row> df = spark.read().format("csv").option("header", "true").load(filePath);


		//I add each array to an arraylist to be normalized later in the code
		arrayOfDfs.add(df);

}

}
	
	


