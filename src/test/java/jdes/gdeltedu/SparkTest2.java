package jdes.gdeltedu;

import static org.junit.Assert.*;
import java.lang.AssertionError;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

import org.junit.Test;
import jdes.gdeltedu.ReFactoredDFI;
import jdes.gdeltedu.CSVFile;


public class SparkTest2 {

	@Test
	public void testCSVFile() {
		File file = new File("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
		
		CSVFile csvFile = new CSVFile();
		
		csvFile.setFilePath("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
		csvFile.setInputFile("filter_edu-000000000000.csv");
		csvFile.setIsCSV(true);
		
		CSVFile result = ReFactoredDFI.findCSVFiles(file);
		
		
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
		
		CSVFile result = ReFactoredDFI.findCSVFiles(file);
		
		
		assertEquals(csvFile.getFilePath(), result.getFilePath());
		assertEquals(csvFile.getInputFile(), result.getInputFile());
		// This should fail.
		assertEquals(csvFile.getIsCSV(), result.getIsCSV());
		
	}
	
	@Test
	public void testCreateDF() {
		File file = new File("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
		
		CSVFile csvFile = new CSVFile();
		
		csvFile.setFilePath("/media/sf_sharedwithVM/TestDataset/filter_edu-000000000000.csv");
		csvFile.setInputFile("filter_edu-000000000000.csv");
		csvFile.setIsCSV(true);
		
		CSVFile result = ReFactoredDFI.findCSVFiles(file);
		
		
		assertEquals(csvFile.getFilePath(), result.getFilePath());
		assertEquals(csvFile.getInputFile(), result.getInputFile());
		assertEquals(csvFile.getIsCSV(), result.getIsCSV());
		
	}

}
