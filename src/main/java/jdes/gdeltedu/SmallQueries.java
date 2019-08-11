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
		Dataset<Row> inputdf = spark.read().format("csv").option("header","true").load(inputDir);

		inputdf.createOrReplaceTempView("freqused");
		
		 
		Dataset<Row> actorAvgTone = inputdf.sqlContext().sql("SELECT Date, AvgTone, NumMentions, "
				+ "SOURCEURL, State, EventCode from freqused");
		
		
				
		
		
		actorAvgTone.show(20);
		
//		actorAvgTone.coalesce(1).write().option("header", "true").mode(SaveMode.Overwrite).csv(output);


	}

}
