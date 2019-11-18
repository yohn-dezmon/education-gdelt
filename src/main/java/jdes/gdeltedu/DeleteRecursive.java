package jdes.gdeltedu;
import java.io.File;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class DeleteRecursive {
	
	boolean deleteDirectory(File directoryToBeDeleted) {
		// Collect an array of files/directories within the given directoryToBeDeleted
	    File[] allContents = directoryToBeDeleted.listFiles();
	    if (allContents != null) {
	        for (File file : allContents) {
	        	// Recursively call this function until the contents of the array of each File object
	        	// is null, indicating it is either an empty directory or a file
	            deleteDirectory(file);
	        }
	    }
	    // Delete either an empty directory or a file
	    return directoryToBeDeleted.delete();
	}
	

		
	}


