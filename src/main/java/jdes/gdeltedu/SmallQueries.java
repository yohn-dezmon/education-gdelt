package jdes.gdeltedu;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

public class SmallQueries {

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

		SparkSession spark = SparkSession.builder().master("local").appName("actor-assessment").
				config("some config", "value").getOrCreate();
		// do I need to change load to csv?
//		Dataset<Row> inputdf = spark.read().format("csv").option("header","true").load(inputDir);

		Dataset<Row> inputdf = spark.read().load(inputDir);
		inputdf.createOrReplaceTempView("gdeltedu");
		
		
		Dataset<Row> CA_curri_URL = inputdf.sqlContext().sql("SELECT DISTINCT(SOURCEURL) from gdeltedu "
				+ "WHERE ActionGeo_ADM1Code like 'USCA' AND SOURCEURL LIKE '%curriculum%' and ActionGeo_CountryCode = 'US'");	
		
		
		
		
		
		CA_curri_URL.coalesce(1).write().option("header", "true").mode(SaveMode.Overwrite).csv(output);


	}

}
