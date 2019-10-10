package jdes.gdeltedu;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.lang.AssertionError;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import jdes.gdeltedu.ReFactoredDFI;
import jdes.gdeltedu.CSVFile;
import jdes.gdeltedu.DeleteRecursive;


import scala.Option;
import scala.Serializable;
import scala.Tuple2;
import scala.reflect.ClassTag;
import jdes.gdeltedu.DFIForSpark2;
import jdes.gdeltedu.TestDataSet;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;

import java.util.Arrays;
import java.util.Collections;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;

public class JunitTests {
	private static SparkSession spark;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		spark = SparkSession.builder().master("local").appName("gdelt-education").
				config("some config", "value").getOrCreate();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		spark.stop();
	}

	
	@Test
	public void testCSVFile() {
		File file = new File("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
		
		CSVFile csvFile = new CSVFile();
		
		csvFile.setFilePath("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
		csvFile.setInputFile("filter_edu-000000000000.csv");
		csvFile.setIsCSV(true);
		
		CSVFile result = DFIForSpark2.findCSVFiles(file);
		
		
		assertEquals(csvFile.getFilePath(), result.getFilePath());
		assertEquals(csvFile.getInputFile(), result.getInputFile());
		assertEquals(csvFile.getIsCSV(), result.getIsCSV());
		
	}
	
	@Test(expected=AssertionError.class)
	public void testCSVFileFail() {
		File file = new File("/media/sf_sharedwithVM/TestDataset/test-non-csv.txt");
		
		CSVFile csvFile = new CSVFile();
		
		csvFile.setFilePath("/media/sf_sharedwithVM/TestDataset/test-non-csv.txt");
		csvFile.setInputFile("test-non-csv.txt");
		// This is incorrect on purpose.
		csvFile.setIsCSV(true);
		
		CSVFile result = DFIForSpark2.findCSVFiles(file);
		
		
		assertEquals(csvFile.getFilePath(), result.getFilePath());
		assertEquals(csvFile.getInputFile(), result.getInputFile());
		// This should fail.
		assertEquals(csvFile.getIsCSV(), result.getIsCSV());
		
	}
	
	@Test
	public void testCreateDataset() {

				
				// Creating a Dataset<Row>
				List<Row> rowss = new ArrayList<>();
				
				rowss.add(RowFactory.create("Andy",32));
				
				StructField[] structFields = new StructField[]{
						// Column name, Data type, nullable, meatdata
			            new StructField("Name", DataTypes.StringType, true, Metadata.empty()),
			            new StructField("Age", DataTypes.IntegerType, true, Metadata.empty())
			    };

			    StructType structType = new StructType(structFields);
				
				Dataset<Row> datasetTest = spark.createDataFrame(rowss, structType);

				// show the dataset for reference
				datasetTest.show();
				
				// create File object (the folder doesn't necessarily have to exist)
				File testCreateDataFile = new File("UnitTestData/testCreateDataset");
				if (testCreateDataFile.exists()) {
					// Delete the subdirectory testCreateDataset if it already exists from previous unit-test runs
					DeleteRecursive dr = new DeleteRecursive();
					dr.deleteDirectory(testCreateDataFile);
					// write a csv file with contents of the Dataset, including the headers assigned in StructField
					datasetTest.write().option("header","true").csv("UnitTestData/testCreateDataset");
				} else {
					datasetTest.write().option("header","true").csv("UnitTestData/testCreateDataset");
				}

		
				String filePath = "UnitTestData/testCreateDataset";
				List<Dataset<Row>> arrayOfDfs = new ArrayList<Dataset<Row>>();
				// create a Dataset using the method defined in the src code (DFIForSpark2)
				// by importing the csv file within the testCreateDataset directory
				DFIForSpark2.createDataFrame(spark, filePath, arrayOfDfs);
				
				// show Dataset for reference
				arrayOfDfs.get(0).show();
				
				// creating tables for a join!
				datasetTest.createOrReplaceTempView("expected");
				arrayOfDfs.get(0).createOrReplaceTempView("actual");
				
				// this should fail if they don't have the same datatypes/number of columns
				Dataset<Row> joinedDataset = datasetTest.sqlContext().sql("SELECT joined.Name, joined.Age"
						+ " from ((SELECT Name, Age from expected) UNION ALL"
						+ " (SELECT Name, Age from actual)) as joined"
						);
				
				joinedDataset.show();
				
				long numberOfRows = joinedDataset.count();
				System.out.println(numberOfRows);
				boolean correctNumber = false;
				if (numberOfRows == 2) {
					correctNumber = true;
				}
				
				assertTrue(correctNumber);

	}
	@Test(expected=AnalysisException.class)
	public void testCreateDatasetErrorNoHeader() {

				
				// Creating a Dataset<Row>
				List<Row> rowss = new ArrayList<>();
				
				rowss.add(RowFactory.create("Andy",32));
				
				StructField[] structFields = new StructField[]{
						// Column name, Data type, nullable, meatdata
			            new StructField("Name", DataTypes.StringType, true, Metadata.empty()),
			            new StructField("Age", DataTypes.IntegerType, true, Metadata.empty())
			    };

			    StructType structType = new StructType(structFields);
				
				Dataset<Row> datasetTest = spark.createDataFrame(rowss, structType);

				// show the dataset for reference
				datasetTest.show();
				
				// create File object (the folder doesn't necessarily have to exist)
				File testCreateDataFile = new File("UnitTestData/testCreateDataset");
				if (testCreateDataFile.exists()) {
					// Delete the subdirectory testCreateDataset if it already exists from previous unit-test runs
					DeleteRecursive dr = new DeleteRecursive();
					dr.deleteDirectory(testCreateDataFile);
					// write a csv file with contents of the Dataset, NOT including the headers assigned in StructField
					datasetTest.write().csv("UnitTestData/testCreateDataset");
				} else {
					datasetTest.write().csv("UnitTestData/testCreateDataset");
				}

		
				String filePath = "UnitTestData/testCreateDataset";
				List<Dataset<Row>> arrayOfDfs = new ArrayList<Dataset<Row>>();
				// create a Dataset using the method defined in the src code (DFIForSpark2)
				// by importing the csv file within the testCreateDataset directory
				DFIForSpark2.createDataFrame(spark, filePath, arrayOfDfs);
				
				// show Dataset for reference
				arrayOfDfs.get(0).show();
				
				// creating tables for a join!
				datasetTest.createOrReplaceTempView("expected");
				arrayOfDfs.get(0).createOrReplaceTempView("actual");
				
				// this should fail if they don't have the same datatypes/number of columns
				Dataset<Row> joinedDataset = datasetTest.sqlContext().sql("SELECT joined.Name, joined.Age"
						+ " from ((SELECT Name, Age from expected) UNION ALL"
						+ " (SELECT Name, Age from actual)) as joined"
						);
				
				joinedDataset.show();
				
				long numberOfRows = joinedDataset.count();
				System.out.println(numberOfRows);
				boolean correctNumber = false;
				if (numberOfRows == 2) {
					correctNumber = true;
				}
				
				assertTrue(correctNumber);

	}
	
	// test filterData method
	@Test
	public void testFilterData() {

				
				// Creating a Dataset<Row>
				List<Row> rowss = new ArrayList<>();
				long dateadded = 20170904093000L;
				
				
				rowss.add(RowFactory.create("686517686",
						"20170904",
						"201709",
						"2017",
						"2017.6685",
						"EDU",
						"SCHOOL",
						"",
						"",
						"",
						"EDU",
						"",
						"",
						"USA",
						"GOLDEN STATE",
						"1",
						020, // EventCode
						020, // EventBaseCode
						02,  // EventRootCode
						"1",
						"10",
						"-1.60984848484849",
						"2",
						"District of Columbia, United States",
						"2",
						"District of Columbia, United States",
						"US",
						"USDC",
						"38.8964",
						"-77.0262",
						dateadded,
						"http://www.nationalreview.com/article/451047/transgender-agenda-schools-kindergarten-california-opt-in-opt-out-state-laws-prevent"));
				
				

			   
				
				// Actual set up
				// Monthyear is included in the original dataset, but is not selected in the SQL statement
				// thus the numbers reflected will be (n-1) for anything after MonthYear
				StructType structType = DataTypes.createStructType( new StructField[] {
						// I set EventCode, EventBaseCode, and EventRootCode to Integers to test if the 
						// sql cast method is working as it should
						DataTypes.createStructField("GLOBALEVENTID", DataTypes.StringType, true), // 1
						DataTypes.createStructField("SQLDATE", DataTypes.StringType, true), // 2
						DataTypes.createStructField("MonthYear", DataTypes.StringType, true), // 3
						DataTypes.createStructField("Year", DataTypes.StringType, true), // 4
						DataTypes.createStructField("FractionDate", DataTypes.StringType, true), // 5
						DataTypes.createStructField("Actor1Code", DataTypes.StringType, true), // 6
						DataTypes.createStructField("Actor1Name", DataTypes.StringType, true), // 7
						DataTypes.createStructField("Actor1CountryCode", DataTypes.StringType, true), // 8
						DataTypes.createStructField("Actor1KnownGroupCode", DataTypes.StringType, true), // 9
						DataTypes.createStructField("Actor1Religion1Code", DataTypes.StringType, true), // 10
						DataTypes.createStructField("Actor1Type1Code", DataTypes.StringType, true), // 11
						DataTypes.createStructField("Actor2Type2Code", DataTypes.StringType, true), // 12
						DataTypes.createStructField("Actor1Type3Code", DataTypes.StringType, true), // 13
						DataTypes.createStructField("Actor2Code", DataTypes.StringType, true), // 14
						DataTypes.createStructField("Actor2Name", DataTypes.StringType, true), // 15
						DataTypes.createStructField("IsRootEvent", DataTypes.StringType, true), // 16
						DataTypes.createStructField("EventCode", DataTypes.IntegerType, true), // 17
						DataTypes.createStructField("EventBaseCode", DataTypes.IntegerType, true),
						DataTypes.createStructField("EventRootCode", DataTypes.IntegerType, true),
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
						// I need to make this a Long type b/c I need to sort on it in the spark SQL...
						DataTypes.createStructField("DATEADDED", DataTypes.LongType, true),
						DataTypes.createStructField("SOURCEURL", DataTypes.StringType, true)

				});
				
				Dataset<Row> datasetTest = spark.createDataFrame(rowss, structType);
				datasetTest.createOrReplaceTempView("gdeltedu");
				
				// show the dataset for reference
				datasetTest.show();
				
				Dataset<Row> actualDataset = DFIForSpark2.filterData(datasetTest);
				
				
				
		
//				Dataset<Row> dfToAppend = dataframe.sqlContext().sql("SELECT GLOBALEVENTID, SQLDATE AS Date, Year, FractionDate, Actor1Code, " +
//						"Actor1Name, Actor1CountryCode, Actor1KnownGroupCode, Actor1Religion1Code, " +
//						"Actor1Type1Code, Actor2Type2Code, Actor1Type3Code, Actor2Code, Actor2Name, " +
//						"IsRootEvent, CAST(EventCode AS STRING) AS EventCode, CAST(EventBaseCode AS STRING) AS EventBaseCode, "
//						+ "CAST(EventRootCode AS STRING) AS EventRootCode, QuadClass, NumMentions, " +
//						"AvgTone, Actor1Geo_Type, Actor1Geo_FullName, ActionGeo_Type, ActionGeo_FullName, " +
//						"ActionGeo_CountryCode, ActionGeo_ADM1Code, ActionGeo_Lat, ActionGeo_Long, DATEADDED AS DateAdded, " +
//						"SOURCEURL from gdeltedu ORDER BY DateAdded");
				
				// Creating a Dataset<Row>
				List<Row> rows2 = new ArrayList<>();
				
				rowss.add(RowFactory.create("686517686", 
						"20170904", 
						"2017", 
						"2017.6685",
						"EDU",
						"SCHOOL",
						"",
						"",
						"",
						"EDU",
						"",
						"",
						"USA",
						"GOLDEN STATE",
						"1",
						"020", // EventCode
						"020", // EventBaseCode
						"02",  // EventRootCode
						"1",
						"10",
						"-1.60984848484849",
						"2",
						"District of Columbia, United States",
						"2",
						"District of Columbia, United States",
						"US",
						"USDC",
						"38.8964",
						"-77.0262",
						dateadded,
						"http://www.nationalreview.com/article/451047/transgender-agenda-schools-kindergarten-california-opt-in-opt-out-state-laws-prevent"));
				
				

			   
				
				// Expected setup
				StructType structType2 = DataTypes.createStructType( new StructField[] {
						// I set EventCode, EventBaseCode, and EventRootCode to Integers to test if the 
						// sql cast method is working as it should
						DataTypes.createStructField("GLOBALEVENTID", DataTypes.StringType, true), // 1
						DataTypes.createStructField("Date", DataTypes.StringType, true), // 2 
						DataTypes.createStructField("Year", DataTypes.StringType, true), 
						DataTypes.createStructField("FractionDate", DataTypes.StringType, true), 
						DataTypes.createStructField("Actor1Code", DataTypes.StringType, true), //
						DataTypes.createStructField("Actor1Name", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor1CountryCode", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor1KnownGroupCode", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor1Religion1Code", DataTypes.StringType, true), //  
						DataTypes.createStructField("Actor1Type1Code", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor2Type2Code", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor1Type3Code", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor2Code", DataTypes.StringType, true), // 
						DataTypes.createStructField("Actor2Name", DataTypes.StringType, true), // 
						DataTypes.createStructField("IsRootEvent", DataTypes.StringType, true), // 
						DataTypes.createStructField("EventCode", DataTypes.StringType, true), // 16
						DataTypes.createStructField("EventBaseCode", DataTypes.StringType, true),  // 17
						DataTypes.createStructField("EventRootCode", DataTypes.StringType, true), // 18
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
						DataTypes.createStructField("DateAdded", DataTypes.LongType, true),
						DataTypes.createStructField("SOURCEURL", DataTypes.StringType, true)

				});
				
				Dataset<Row> expectedDataset = spark.createDataFrame(rows2, structType2);
				
				DFIForSpark2.dataTypePrint(expectedDataset);
				
				String[] expected_cols = expectedDataset.columns();
				String[] actual_cols = actualDataset.columns();
				
				assertEquals(expected_cols[16], actual_cols[16]);
				assertEquals(expected_cols[17], actual_cols[17]);
				assertEquals(expected_cols[18], actual_cols[18]);
				
				
				
//				Tuple2<String, String> expecteddtypes = expectedDataset.dtypes();
				assertEquals(expectedDataset.dtypes(), actualDataset.dtypes());
				
				
//				long numberOfRows = joinedDataset.count();
//				System.out.println(numberOfRows);
//				boolean correctNumber = false;
//				if (numberOfRows == 2) {
//					correctNumber = true;
//				}
//				
//				assertTrue(correctNumber);

	}
	
	// test appendData method 

	

}
