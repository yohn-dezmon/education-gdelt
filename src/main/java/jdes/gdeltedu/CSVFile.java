package jdes.gdeltedu;

/*
* This class was created to be able to return multiple values of different
* datatypes from the findCSVFiles method within the ReFactoredDFI class.
*/
public class CSVFile {
	boolean isCSV;
	String inputFile;
	String filePath;

	public boolean getIsCSV() {
		return isCSV;
	}

	public void setIsCSV(boolean newBool) {
		this.isCSV = newBool;
	}

	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String newName) {
		this.inputFile = newName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String newPath) {
		this.filePath = newPath;
	}



}
