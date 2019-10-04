package jdes.gdeltedu;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import scala.Tuple2;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;



public class ReFactoredDFI {
	// I'm going to use this class with Spark 1.6.0 to do unit testing of the spark
	// functionality within the code. The actual 'datFrameInput' and 'FullDataOutput' should
	// be run with Spark 2.4.0

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

	public static String removeQuotes(String string) {
		String withoutQuotes = string.replace("\"", "");
		return withoutQuotes;
	};

	public static void createDataFrame(String filePath, JavaSparkContext sc, SQLContext sql,
			List<DataFrame> arrayOfDfs) {
		JavaRDD<String> lines = sc.textFile(filePath);
		JavaRDD<Row> rows = lines.map((String string) -> {
			String[] parts = string.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

			if (parts.length != 32) {
                // Handle bad data
                return RowFactory.create("", "", "", "", "", "", "", "", "", "",
                		"", "", "", "", "", "", "", "", "", "",
                		"", "", "", "", "", "", "", "", "", "",
                		"","");
            } else {
			return RowFactory.create(parts[0].toString(),
					parts[1].toString(),parts[2].toString(), parts[3].toString(),
				 parts[4].toString(), parts[5].toString(), parts[6].toString(),
				 parts[7].toString(), parts[8].toString(), parts[9].toString(),
				 parts[10].toString(), parts[11].toString(), parts[12].toString(),
				 parts[13].toString(), parts[14].toString(), parts[15].toString(),
				 parts[16].toString(), parts[17].toString(), parts[18].toString(),
				 parts[19].toString(), parts[20].toString(), parts[21].toString(),
				 parts[22].toString(), parts[23].toString(), parts[24].toString(),
				 parts[25].toString(), parts[26].toString(), parts[27].toString(),
				 parts[28].toString(), parts[29].toString(), parts[30].toString(),
				 parts[31].toString());
            }});

		StructType schema = DataTypes.createStructType( new StructField[] {
				// what does the false indicate? it is the boolean variable for *NULLABLE
				// we want there to be null values, so set the last parameter to true!

				// ,,ActionGeo_ADM1Code,ActionGeo_Lat,ActionGeo_Long,DATEADDED,SOURCEURL
				DataTypes.createStructField("GLOBALEVENTID", DataTypes.StringType, true),
				DataTypes.createStructField("SQLDATE", DataTypes.StringType, true),
				DataTypes.createStructField("MonthYear", DataTypes.StringType, true),
				DataTypes.createStructField("Year", DataTypes.StringType, true),
				DataTypes.createStructField("FractionDate", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Code", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Name", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1CountryCode", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1KnownGroupCode", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Religion1Code", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Type1Code", DataTypes.StringType, true),
				DataTypes.createStructField("Actor2Type2Code", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Type3Code", DataTypes.StringType, true),
				DataTypes.createStructField("Actor2Code", DataTypes.StringType, true),
				DataTypes.createStructField("Actor2Name", DataTypes.StringType, true),
				DataTypes.createStructField("IsRootEvent", DataTypes.IntegerType, true),
				DataTypes.createStructField("EventCode", DataTypes.StringType, true),
				DataTypes.createStructField("EventBaseCode", DataTypes.StringType, true),
				DataTypes.createStructField("EventRootCode", DataTypes.StringType, true),
				DataTypes.createStructField("QuadClass", DataTypes.StringType, true),
				DataTypes.createStructField("NumMentions", DataTypes.StringType, true),
				DataTypes.createStructField("AvgTone", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Geo_Type", DataTypes.StringType, true),
				DataTypes.createStructField("Actor1Geo_FullName", DataTypes.StringType, true),
				DataTypes.createStructField("ActionGeo_Type", DataTypes.StringType, true),
				DataTypes.createStructField("ActionGeo_FullName", DataTypes.StringType, true),
				DataTypes.createStructField("ActionGeo_CountryCode", DataTypes.StringType, true),
				DataTypes.createStructField("ActionGeo_ADM1Code", DataTypes.StringType, true),
				DataTypes.createStructField("ActionGeo_Lat", DataTypes.StringType, true),
				DataTypes.createStructField("ActionGeo_Long", DataTypes.StringType, true),
				DataTypes.createStructField("DATEADDED", DataTypes.StringType, true),
				DataTypes.createStructField("SOURCEURL", DataTypes.StringType, true)

		});

		DataFrame dataFrame = sql.createDataFrame(rows, schema);



//		Dataset<Row> df = spark.read().format("csv").option("inferSchema", "true").option("header", "true").load(filePath);

		//I add each array to an arraylist to be normalized later in the code
		arrayOfDfs.add(dataFrame);

	}

	public static DataFrame filterData(DataFrame dataframe) {
		/*
		 *
		 */

		// ok I need to get rid of Dataset... and use Dataframe instead...
		// CAST(EventBaseCode AS STRING) AS
		// CAST(EventRootCode AS STRING) AS
		// CAST(EventCode AS STRING) AS
		DataFrame dfToAppend = dataframe.sqlContext().sql("SELECT GLOBALEVENTID, SQLDATE, Year, FractionDate, Actor1Code, " +
				"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " +
				"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " +
				"IsRootEvent, EventCode, EventBaseCode, "
				+ "EventRootCode, QuadClass, NumMentions, " +
				"AvgTone, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " +
				"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, DATEADDED, " +
				"SOURCEURL from gdeltedu");

		dfToAppend.show();

		return dfToAppend;

	}

	public static void appendData(DataFrame dfToAppend, String output) {
		//  output in HDFS: /user/vmuser/gdelt
		// local output: /home/vmuser/Desktop
		dfToAppend.write().mode(SaveMode.Append).parquet(output);

	}

	public static void dataTypePrint(DataFrame dataset) {
		Tuple2<String, String>[] tuples = dataset.dtypes();

		for (Tuple2<String, String> tuple: tuples) {
		System.out.println(tuple);
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


			// setMaster("local").setAppName("gdelt-input")
			SparkConf conf = new SparkConf();
			JavaSparkContext sc = new JavaSparkContext("local", "gdelt-input", conf);
			SQLContext sql = new SQLContext(sc);


			sql.udf().register("todate", (dateInt) -> {
	            // Example input: 20190710
				String dateStr = String.valueOf(dateInt);

	            LocalDate goodDate = LocalDate.parse(dateStr,
	            DateTimeFormatter.ofPattern("yyyyMMdd"));

	            Date date = Date.valueOf(goodDate);
	            return date;
			}, DataTypes.DateType);

			// creates an empty list to add the dataframes to
			List<DataFrame> arrayOfDfs = new ArrayList<DataFrame>();

			// test dir = /media/sf_sharedwithVM/TestDataset/
			// actual dir = /media/sf_sharedwithVM/PersonalProjectData/
			File[] folder = new File(inputDir).listFiles();
			for (File file : folder) {
					System.out.println(file.getAbsolutePath());
					// CSVFile is a class I created such that findCSVFiles can return multiple values of different types.
					CSVFile potentialCSVFile = findCSVFiles(file);
					if (potentialCSVFile.getIsCSV() == true) {
						// String inputFile, JavaSparkContext sc, SQLContext sql, String filePath,
						// List<DataFrame> arrayOfDfs
						createDataFrame(potentialCSVFile.getFilePath(), sc, sql,
								arrayOfDfs);
					}
					}


			for (DataFrame dataframe : arrayOfDfs) {
				// this creates a table gdeltedu that I can run sql queries on
				dataframe.registerTempTable("gdeltedu");
				dataTypePrint(dataframe);
				DataFrame dfToAppend = filterData(dataframe);
//				appendData(dataframe, output);

			}

		}


}
