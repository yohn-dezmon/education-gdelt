package jdes.gdeltedu;

import java.io.File;
import java.sql.Date;
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
import org.apache.spark.sql.types.DataTypes;

import scala.Tuple2;

public class DFIForSpark2 {

	public static CSVFile findCSVFiles(File file) {
		CSVFile csvFile = new CSVFile();

		boolean isCSV = false;
		Pattern csvp = Pattern.compile(".*\\.csv");

		String filePath = file.getAbsolutePath();
		String inputFile = file.getName();
		Matcher m = csvp.matcher(inputFile);
		if (m.matches()) {
			isCSV = true;
		}

		csvFile.setFilePath(filePath);
		csvFile.setInputFile(inputFile);
		csvFile.setIsCSV(isCSV);

		return csvFile;
	}

	public static void createDataFrame(SparkSession spark, String filePath,
			List<Dataset<Row>> arrayOfDfs) {

		Dataset<Row> df = spark.read().format("csv").option("inferSchema", "true").option("header", "true").load(filePath);

		//I add each array to an arraylist to be normalized later in the code
		arrayOfDfs.add(df);

	}

	public static Dataset<Row> filterData(Dataset<Row> dataframe) {
		
		dataTypePrint(dataframe);


		Dataset<Row> dfToAppend = dataframe.sqlContext().sql("SELECT GLOBALEVENTID, SQLDATE AS Date, Year, FractionDate, Actor1Code, " +
				"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " +
				"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " +
				"IsRootEvent, CAST(EventCode AS STRING) AS EventCode, CAST(EventBaseCode AS STRING) AS EventBaseCode, "
				+ "CAST(EventRootCode AS STRING) AS EventRootCode, QuadClass, NumMentions, " +
				"AvgTone, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " +
				"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, DATEADDED AS DateAdded, " +
				"SOURCEURL from gdeltedu ORDER BY DateAdded");



		return dfToAppend;

	}

	public static void appendData(Dataset<Row> dfToAppend, String output) {
		//  output in HDFS: /user/vmuser/gdelt
		// local output: /home/vmuser/Desktop
		dfToAppend.write().mode(SaveMode.Append).parquet(output);

	}

	public static void dataTypePrint(Dataset<Row> dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();
		int col_indx = 1;

		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple + " " + col_indx);
		col_indx += 1;
		}
        return;
    }

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

			spark.udf().register("todate", (dateInt) -> {
	            // Example input: 20190710
				String dateStr = String.valueOf(dateInt);

	            LocalDate goodDate = LocalDate.parse(dateStr,
	            DateTimeFormatter.ofPattern("yyyyMMdd"));

	            Date date = Date.valueOf(goodDate);
	            return date;
			}, DataTypes.DateType);

			// creates an empty list to add the dataframes to
			List<Dataset<Row>> arrayOfDfs = new ArrayList<Dataset<Row>>();

			// test dir = /media/sf_sharedwithVM/TestDataset/
			// actual dir = /media/sf_sharedwithVM/PersonalProjectData/
			File[] folder = new File(inputDir).listFiles();
			for (File file : folder) {
					// CSVFile is a class I created such that findCSVFiles can return multiple values of different types.
					CSVFile potentialCSVFile = findCSVFiles(file);
					if (potentialCSVFile.getIsCSV() == true) {
						createDataFrame(spark, potentialCSVFile.getFilePath(),
								arrayOfDfs);
					}
					}


			for (Dataset<Row> dataframe : arrayOfDfs) {
				// this creates a table gdeltedu that I can run sql queries on
				dataframe.createOrReplaceTempView("gdeltedu");

				Dataset<Row> dfToAppend = filterData(dataframe);
				appendData(dfToAppend, output);

			}

		}


}
