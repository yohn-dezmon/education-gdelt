package jdes.gdeltedu;


import org.apache.hadoop.conf.Configuration;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import scala.Tuple2;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


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

		// Create the timestamp creator UDF
        spark.udf().register("totimestamp", (Long date) -> {
        		String dateStr = date.toString();
                // Example input: 20150422173000
            	// yyyyMMddHHmmss
                LocalDateTime fulltime = LocalDateTime.parse(dateStr,
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                Timestamp timestamp = Timestamp.valueOf(fulltime);

                return timestamp;
            
        }, DataTypes.TimestampType);
        

        spark.udf().register("substr", (String EventCode) -> {
            
    		if (EventCode.charAt(0) == '0') {
    			EventCode = EventCode.substring(1);
    		}

            return EventCode;
        
    }, DataTypes.StringType);
        
        // If I export this into MySQL again, do not use this UDF... as it is slowing down performance 
        spark.udf().register("substr2", (String ActionGeo_ADM1Code) -> {
			if (ActionGeo_ADM1Code == "" || ActionGeo_ADM1Code == null) {
				return ActionGeo_ADM1Code;
			}
            String prefix = ActionGeo_ADM1Code.substring(0,2);
    		if (prefix == "US") {
    			ActionGeo_ADM1Code = ActionGeo_ADM1Code.substring(2);
    		}

            return ActionGeo_ADM1Code;
        
    }, DataTypes.StringType);
        
		
		//  rowkey if I decide to use HBase CONCAT(GLOBALEVENTID, Date, EventCode, NumMentions) as RowKey
		Dataset<Row> gdeltFreqUsed = inputdf.sqlContext().sql("Select "
				+ " GLOBALEVENTID, Year, Date, DateAdded, " + 
				"Actor1Code, Actor1Name, " + 
				" Actor2Code, Actor2Name, " + 
				"IsRootEvent, substr(EventCode) as EventCode,  QuadClass, NumMentions, " + 
				"AvgTone, ActionGeo_FullName, " + 
				"ActionGeo_CountryCode, substr2(ActionGeo_ADM1Code) as State,  " + 
				"SOURCEURL from gdeltedu ORDER BY DateAdded");
		
		
		
		
		String table = "gdelt.freqused";
		String lessFreqTable = "gdelt.lessfreqused";
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", "root");
		connectionProperties.put("password", "");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		gdeltFreqUsed.write().mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/gdelt", table, connectionProperties);
		
		Dataset<Row> gdeltLessFreqUsed = inputdf.sqlContext().sql("Select GLOBALEVENTID, FractionDate, "
				+ "Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, "
				+ "Actor1Type1Code, Actor1Type3Code,  Actor2Type2Code, "
				+ "EventBaseCode, EventRootCode, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, "
				+ "ActionGeo_Lat, ActionGeo_Long from gdeltedu ORDER BY FractionDate");
		
		gdeltLessFreqUsed.write().mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/gdelt", lessFreqTable, connectionProperties);

		
	}
	


	
	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();
		
		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
		}
        return;
    }

}