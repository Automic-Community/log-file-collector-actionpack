package com.uc4.ara.feature.logcollector.structure;

public class Info {

	private int numberOfFiles;
	private String outputPath;
	
	public int getNumberOfFiles()
	{
		return this.numberOfFiles;
	}
	
	public String getOutputPath()
	{
		return this.outputPath;
	}
	
	public void setNumberOfFiles(int numberOfFiles)
	{
		this.numberOfFiles = numberOfFiles;
	}
	
	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}
}
