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

/**
* The FullDataOutput class takes a directory containing parquet files as its
* input and loads them into a single Dataset, which is then transformed for
* storage within MySQL.
*/

public class FullDataOutput {

	public static void main(String[] args) {
		// The input and output directories must be supplied as command line arguments.
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

		// Here all of the individual parquet files within the input dir are loaded
		// into one Dataset.
		Dataset<Row> inputdf = spark.read().load(inputDir);

		// Creating a temporary table to use with Spark SQL.
		inputdf.createOrReplaceTempView("gdeltedu");

		// Timestamp creator UDF, not currently used as dates are easily converted to
		// date time objects in Pandas.
        spark.udf().register("totimestamp", (Long date) -> {
        		String dateStr = date.toString();
                // Example input: 20150422173000
            	// yyyyMMddHHmmss
                LocalDateTime fulltime = LocalDateTime.parse(dateStr,
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                Timestamp timestamp = Timestamp.valueOf(fulltime);

                return timestamp;

        }, DataTypes.TimestampType);

        // UDF to remove 0 from the beginning of the EventCodes to prevent duplication.
        spark.udf().register("substr", (String EventCode) -> {

    		if (EventCode.charAt(0) == '0') {
    			EventCode = EventCode.substring(1);
    		}

            return EventCode;

    }, DataTypes.StringType);

        // UDF to extract state from country-state code (ActionGeo_ADM1Code).
        spark.udf().register("substr2", (String ActionGeo_ADM1Code) -> {
			if (ActionGeo_ADM1Code == "" || ActionGeo_ADM1Code == null) {
				return ActionGeo_ADM1Code;
			}
			// Trim leading white space.
			ActionGeo_ADM1Code = ActionGeo_ADM1Code.trim();
			String state = ActionGeo_ADM1Code;

			// Validate prefix.
            String prefix = ActionGeo_ADM1Code.substring(0,2);
    		if (prefix.equals("US")) {
    			state = ActionGeo_ADM1Code.substring(2);
    		}

            return state;

    }, DataTypes.StringType);


		// Rowkey if I decide to use HBase CONCAT(GLOBALEVENTID, Date, EventCode, NumMentions) as RowKey
		// gdeltFreqUsed will be put into a separate MySQL table from lessFreqTable
		// to improve query speed as less data needs to be traversed.
		Dataset<Row> gdeltFreqUsed = inputdf.sqlContext().sql("Select "
				+ " GLOBALEVENTID, Year, Date, DateAdded, " +
				"Actor1Code, Actor1Name, " +
				" Actor2Code, Actor2Name, " +
				"IsRootEvent, substr(EventCode) as EventCode,  QuadClass, NumMentions, " +
				"AvgTone, ActionGeo_FullName, " +
				"ActionGeo_CountryCode, substr2(ActionGeo_ADM1Code) as State,  " +
				"SOURCEURL from gdeltedu ORDER BY DateAdded");

		gdeltFreqUsed.show();

		// Setting up the MySQL tables where the Datasets will be exported to.
		String table = "gdelt.freqused";
		String lessFreqTable = "gdelt.lessfreqused";
		String testTable = "gdelt.testing";
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", "root");
		connectionProperties.put("password", "");
		try {
			Class.forName("com.mysql.jdbc.Driver");

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		// writing the gdeltFreqUsed Dataset to the freqused MySQL table.
//		gdeltFreqUsed.write().mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/gdelt", table, connectionProperties);

		// These fields will provide additional information to the analytics I'll be running
		// and will most likely not be accessed in the majority of my queries.
		Dataset<Row> gdeltLessFreqUsed = inputdf.sqlContext().sql("Select GLOBALEVENTID, FractionDate, "
				+ "Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, "
				+ "Actor1Type1Code, Actor1Type3Code,  Actor2Type2Code, "
				+ "EventBaseCode, EventRootCode, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, "
				+ "ActionGeo_Lat, ActionGeo_Long from gdeltedu ORDER BY FractionDate");

		// writing the gdeltLessFreqUsed Dataset to the lessfreqused MySQL table.
//		gdeltLessFreqUsed.write().mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/gdelt", lessFreqTable, connectionProperties);


//		Dataset<Row> gdelttest = inputdf.sqlContext().sql("Select GLOBALEVENTID from gdeltedu LIMIT 10");
//		gdelttest.write().mode(SaveMode.Overwrite).jdbc("jdbc:mysql://localhost:3306/gdelt", testTable, connectionProperties);


	}




	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();

		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
		}
        return;
    }

}
