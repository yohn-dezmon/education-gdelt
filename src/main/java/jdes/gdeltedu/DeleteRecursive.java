package jdes.gdeltedu;
import java.io.File;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class DeleteRecursive {
	
	boolean deleteDirectory(File directoryToBeDeleted) {
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	            deleteDirectory(file);
	        }
	    }
	    return directoryToBeDeleted.delete();
	}
	
	
	
//	public static void deleteRecurisve(File[] parentFolderFiles, Dataset<Row> datasetTest) {
//		
//		if (parentFolderFiles.length == 0) {
//			datasetTest.write().csv("UnitTestData/testCreateDataset");
//		}
//		if (parentFolderFiles.length != 0) {
//			for (File file: parentFolderFiles) {
//				if (file.isDirectory()) {
//					File[] subdirFiles = file.listFiles();
//					deleteRecursive(subdirFiles, datasetTest);
//				}
//			}
//		}
		
	}


