package jdes.gdeltedu;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.lang.AssertionError;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

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

				
				// Trying to create a Dataset<Row> instead of Dataset<TestDataSet>...
				List<Row> rowss = new ArrayList<>();
				
				rowss.add(RowFactory.create("Andy",32));
				
				StructField[] structFields = new StructField[]{
			            new StructField("Name", DataTypes.StringType, true, Metadata.empty()),
			            new StructField("Age", DataTypes.IntegerType, true, Metadata.empty())
			    };

			    StructType structType = new StructType(structFields);
				
				Dataset<Row> datasetTest = spark.createDataFrame(rowss, structType);

				
				datasetTest.show();
				// Deleting the contents of the folder, then deleting the folder
				// I need to check to see if UnitTestData has any sub-folders
				
				File testCreateDataFile = new File("UnitTestData/testCreateDataset");
				if (testCreateDataFile.exists()) {
					DeleteRecursive dr = new DeleteRecursive();
					dr.deleteDirectory(testCreateDataFile);
					datasetTest.write().option("header","true").csv("UnitTestData/testCreateDataset");
				} else {
					datasetTest.write().option("header","true").csv("UnitTestData/testCreateDataset");
				}

		
				String filePath = "UnitTestData/testCreateDataset";
				List<Dataset<Row>> arrayOfDfs = new ArrayList<Dataset<Row>>();
				
				DFIForSpark2.createDataFrame(spark, filePath, arrayOfDfs);
				
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

}
