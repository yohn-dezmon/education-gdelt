package jdes.gdeltedu;

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
