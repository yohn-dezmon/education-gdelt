package jdes.gdeltedu;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import scala.Tuple2;
import java.time.LocalDate;
import java.sql.Date;
import java.sql.Timestamp;

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
		
		spark.udf().register("todate", (dateInt) -> { 
            // Example input: 20190710
			String dateStr = String.valueOf(dateInt);
			
            LocalDate goodDate = LocalDate.parse(dateStr,
            DateTimeFormatter.ofPattern("yyyyMMdd"));
            
            Date date = Date.valueOf(goodDate);
            return date;
        
    }, DataTypes.DateType);
		
		
		
		
		for (Dataset<Row> dataframe : arrayOfDfs) {
			// this creates a table nasdaq that I can run sql queries on
			dataframe.createOrReplaceTempView("firsttable");
			
			Dataset<Row> dfdf = dataframe.sqlContext().sql("SELECT GLOBALEVENTID,CAST(SQLDATE AS STRING) AS strdate,Year,FractionDate,Actor1Code, " + 
					"Actor1Name,Actor1CountryCode,Actor1KnownGroupCode,Actor1Religion1Code, " + 
					"Actor1Type1Code,Actor2Type2Code,Actor1Type3Code,Actor2Code,Actor2Name, " + 
					"IsRootEvent,EventCode,EventBaseCode,EventRootCode,QuadClass,NumMentions, " + 
					"AvgTone,Actor1Geo_Type,Actor1Geo_FullName,ActionGeo_Type,ActionGeo_FullName, " + 
					"ActionGeo_CountryCode,ActionGeo_ADM1Code,ActionGeo_Lat,ActionGeo_Long,DATEADDED, " + 
					"SOURCEURL from firsttable");
			
			dfdf.createOrReplaceTempView("secondtable");
			
			// generating dataset for query by states...
			Dataset<Row> dfToAppend = dataframe.sqlContext().sql("Select GLOBALEVENTID, todate(strdate) as Date,Year,FractionDate,Actor1Code, " + 
					"Actor1Name,Actor1CountryCode,Actor1KnownGroupCode,Actor1Religion1Code, " + 
					"Actor1Type1Code,Actor2Type2Code,Actor1Type3Code,Actor2Code,Actor2Name, " + 
					"IsRootEvent,EventCode,EventBaseCode,EventRootCode,QuadClass,NumMentions, " + 
					"AvgTone,Actor1Geo_Type,Actor1Geo_FullName,ActionGeo_Type,ActionGeo_FullName, " + 
					"ActionGeo_CountryCode,ActionGeo_ADM1Code,ActionGeo_Lat,ActionGeo_Long,DATEADDED, " + 
					"SOURCEURL from secondtable");
			
			
			// this may be useful late for example for NumMentions! [gives basic statistics per column]
//			dfToAppend.describe("SQLDATE","MonthYear","Year","FractionDate").show();
//			Dataset<Row> dfStrDate = dfToAppend.withColumn("SQLDATE", dfToAppend.col("SQLDATE").cast(DataTypes.StringType));
			
//			dfdf.show();
			dfToAppend.show();
			
//			dataTypePrint(dfDateType);
			dataTypePrint(dfToAppend);
//			dataTypePrint(dfdf);
			
//			dfDateType.show();

			
			// local output: /home/vmuser/stockdata/joineddata/nasdaq
			// this saves individual parquet files into a folder, that can later be accessed as one dataframe
//			dfToAppend.write().mode(SaveMode.Append).parquet(output);

		}
	
		
		
	}
	
	public static void createDataFrame(SparkSession spark, String filePath, String inputFile,
			List<Dataset<Row>> arrayOfDfs) {

		Dataset<Row> df = spark.read().format("csv").option("inferSchema", "true").option("header", "true").load(filePath);
		
		
		

		//I add each array to an arraylist to be normalized later in the code
		arrayOfDfs.add(df);

}
	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();
		
		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
		}
        return;
    }

}
	
	


