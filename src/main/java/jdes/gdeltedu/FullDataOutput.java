package jdes.gdeltedu;


import org.apache.hadoop.conf.Configuration;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import scala.Tuple2;



public class FullDataOutput {
	
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
		
		
		
		SparkSession spark = SparkSession.builder().master("local").appName("gdelt-education-output").
				config("some config", "value").getOrCreate();
		// option("inferSchema", "false")
		Dataset<Row> inputdf = spark.read().format("com.databricks.spark.avro").load(inputDir);
		inputdf.createOrReplaceTempView("gdeltedu");

		// CONCAT(GLOBALEVENTID, SQLDATE, EventCode, ActionGeo_ADM1Code) as RowKey,
		Dataset<Row> df = inputdf.sqlContext().sql("Select "
				+ "GLOBALEVENTID, Year, FractionDate, Actor1Code, Date, DateAdded, " + 
				"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " + 
				"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " + 
				"IsRootEvent, CAST(EventCode AS STRING) EventCode, CAST(EventBaseCode AS STRING) as EventBaseCode,"
				+ " CAST(EventRootCode AS STRING) AS EventRootCode, QuadClass, NumMentions, " + 
				"AvgTone,Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " + 
				"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, " + 
				"SOURCEURL from gdeltedu ORDER BY DATEADDED");
		
		df.createOrReplaceTempView("gdeltViaAvro");
		
		//  rowkey if I decide to use HBase CONCAT(GLOBALEVENTID, Date, EventCode, NumMentions) as RowKey
		
//		Dataset<Row> gdeltFreqUsed = inputdf.sqlContext().sql("Select "
//				+ " GLOBALEVENTID, Year, Actor1Code, Date, DateAdded, " + 
//				"Actor1Name, Actor1Code, " + 
//				" Actor2Code, Actor2Name, " + 
//				"IsRootEvent, EventCode,  QuadClass, NumMentions, " + 
//				"AvgTone, ActionGeo_FullName, " + 
//				"ActionGeo_CountryCode, ActionGeo_ADM1Code,  " + 
//				"SOURCEURL from gdeltedu ORDER BY DateAdded");
//		
//		Dataset<Row> gdeltLessFreqUsed = inputdf.sqlContext().sql("Select GLOBALEVENTID, FractionDate, "
//				+ "Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, "
//				+ "Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, "
//				+ "EventBaseCode, EventRootCode, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, "
//				+ "ActionGeo_Lat, ActionGeo_Long from gdeltedu ORDER BY FractionDate");
		
		
		df.show();
		
		dataTypePrint(df);
//		dataTypePrint(gdeltLessFreqUsed);
		
//		Dataset<Row> df3 = df.withColumn("RowKey", df.col(""+"SQLDATE"+"Actor1Name"+""+""));
		

//		df2.coalesce(1).write().format("com.databricks.spark.avro").mode(SaveMode.Overwrite).save(output);
//		df2.coalesce(1).write().mode(SaveMode.Overwrite).csv(output);
		
		
		
		
		
	}
	
	
	
	
	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();
		
		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
		}
        return;
    }

}
