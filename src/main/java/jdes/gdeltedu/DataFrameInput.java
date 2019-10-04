package jdes.gdeltedu;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
import java.time.LocalDateTime;
import java.sql.Date;
import java.sql.Timestamp;

/**
* The DataFrameInput class takes a directory containing csv files as its
* input, loads them into a Dataset, and then appends that Dataset to
* a parquet file within the output directory. The refactored version of
* this code for modularity/unittesting resides in the ReFactoredDFI class.
*/

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

		// Creates an empty list to add the Datasets to.
		List<Dataset<Row>> arrayOfDS = new ArrayList<Dataset<Row>>();
		// Creation of a regex pattern to search for csv files within the input dir.
		Pattern csvp = Pattern.compile(".*\\.csv");

		// Test dir = /media/sf_sharedwithVM/TestDataset/
		// Actual dir = /media/sf_sharedwithVM/PersonalProjectData/
		File[] folder = new File(inputDir).listFiles();
		for (File file : folder) {
				String filePath = file.getAbsolutePath();
				String inputFile = file.getName();
				Matcher m = csvp.matcher(inputFile);
				// Create a Dataset if the file is a .csv file.
					if (m.matches()) {
						createDataSet(spark, filePath, inputFile, arrayOfDS);
					}
				}
		// This udf converts a string of format 20190710 to a DateType object However
		// I decided not to use it to improve efficiency.
		spark.udf().register("todate", (dateInt) -> {
            // Example input: 20190710
			String dateStr = String.valueOf(dateInt);

            LocalDate goodDate = LocalDate.parse(dateStr,
            DateTimeFormatter.ofPattern("yyyyMMdd"));

            Date date = Date.valueOf(goodDate);
            return date;

    }, DataTypes.DateType);

		for (Dataset<Row> dataframe : arrayOfDS) {
			// This creates a table gdeltedu that I can run sql queries on.
			dataframe.createOrReplaceTempView("gdeltedu");

			// Here the original data is filtered for the desired columns and
			// several columns are transformed using SQL functions.
			Dataset<Row> dfToAppend = dataframe.sqlContext().sql("SELECT GLOBALEVENTID, SQLDATE AS Date, Year, FractionDate, Actor1Code, " +
					"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " +
					"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " +
					"IsRootEvent, CAST(EventCode AS STRING) AS EventCode, CAST(EventBaseCode AS STRING) AS EventBaseCode, "
					+ "CAST(EventRootCode AS STRING) AS EventRootCode, QuadClass, NumMentions, " +
					"AvgTone, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " +
					"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, DATEADDED AS DateAdded, " +
					"SOURCEURL from gdeltedu ORDER BY DateAdded");

			// output in HDFS: /user/vmuser/gdelt
			// local output: /home/vmuser/Desktop
			dataTypePrint(dfToAppend);
//			dfToAppend.write().mode(SaveMode.Append).parquet(output);
		}

	}

	public static void createDataSet(SparkSession spark, String filePath, String inputFile,
			List<Dataset<Row>> arrayOfDfs) {

		Dataset<Row> df = spark.read().format("csv").option("inferSchema", "true").option("header", "true").load(filePath);

		// I add each array to an arraylist to be normalized later in the code.
		arrayOfDfs.add(df);

}

/**
* The method dataTypePrint is not used currently in the code but is useful to see the
* types of the columns within the Dataset.
*/
	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();

		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
		}
        return;
    }

}
