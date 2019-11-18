package jdes.gdeltedu;

import static org.junit.Assert.*;
import java.lang.AssertionError;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import org.junit.Before;
import jdes.gdeltedu.ReFactoredDFI;
import jdes.gdeltedu.CSVFile;

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

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;

import com.holdenkarau.spark.testing.JavaRDDComparisons;
import com.holdenkarau.spark.testing.SharedJavaSparkContext;



public class SparkTest2 extends SharedJavaSparkContext 
 implements Serializable {
	// I need to instantiate Spark here so that it doesn't get re-run for each 
	// unit test
	private static final long serialVersionUID = -5681683598336701496L;
	
//	@Before
//	public void runBefore() {
//		SparkSession spark = SparkSession.builder().master("local").appName("gdelt-education").
//				config("some config", "value").getOrCreate();
//		
//	}

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
	public void testCreateDF() {
//		File file = new File("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
//		
//		CSVFile csvFile = new CSVFile();
//		
//		csvFile.setFilePath("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
//		csvFile.setInputFile("filter_edu-000000000000.csv");
//		csvFile.setIsCSV(true);
		
		// Create an instance of a Bean class
		TestDataSet person = new TestDataSet();
		person.setName("Andy");
		person.setAge(32);
		
		List<String> input = Arrays.asList("1\tHeart", "2\tDiamonds");
	    JavaRDD<String> inputRDD = jsc().parallelize(input);
	    
//		Encoder<TestDataSet> personEncoder = Encoders.bean(TestDataSet.class);
//		Dataset<TestDataSet> javaBeanDS = jsc().createDataset(
//		  Collections.singletonList(person),
//		  personEncoder
//		);
		
		List<Dataset<Row>> arrayOfDfs = new ArrayList<Dataset<Row>>();
		
		
		
		
		// SparkSession
		// String
		// String
		// List<Dataset<Row>> 
		CSVFile result = DFIForSpark2.createDataFrame(
				spark,
				String filePath,
				String inputFile,
				List<Dataset<Row>> arrayOfDfs);
		
		
		assertEquals(csvFile.getFilePath(), result.getFilePath());
		assertEquals(csvFile.getInputFile(), result.getInputFile());
		assertEquals(csvFile.getIsCSV(), result.getIsCSV());
		
	}

}
