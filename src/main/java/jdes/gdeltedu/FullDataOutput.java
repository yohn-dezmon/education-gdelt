package jdes.gdeltedu;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

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
		
		Dataset<Row> inputdf = spark.read().load(inputDir);
		inputdf.createOrReplaceTempView("gdeltedu");
		
		Dataset<Row> df = inputdf.sqlContext().sql("Select GLOBALEVENTID, Date, Year, FractionDate, Actor1Code, " + 
				"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " + 
				"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " + 
				"IsRootEvent, EventCode, EventBaseCode, EventRootCode, QuadClass, NumMentions, " + 
				"AvgTone,Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " + 
				"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, DateAdded, " + 
				"SOURCEURL from gdeltedu order by Date");
		
		
		df.createOrReplaceTempView("gdeltedu2");
		
		Dataset<Row> df2 = df.sqlContext().sql("Select GLOBALEVENTID, Date, Year, FractionDate, Actor1Code, " + 
				"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " + 
				"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " + 
				"IsRootEvent, EventCode, EventBaseCode, EventRootCode, QuadClass, NumMentions, " + 
				"AvgTone,Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " + 
				"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, DateAdded, " + 
				"SOURCEURL from gdeltedu2 order by Date");
		
		df2.show();
		
		dataTypePrint(df);
		
		
		
	}
	
	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();
		
		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
		}
        return;
    }

}
