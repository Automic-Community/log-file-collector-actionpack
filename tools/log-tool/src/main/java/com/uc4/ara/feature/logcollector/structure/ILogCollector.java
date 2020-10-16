package com.uc4.ara.feature.logcollector.structure;

public interface ILogCollector {
	
	public String[] getFilesInPath(Settings settings);
	public String[] getAffectedFiles(Settings settings, String[] fileNames);

	public String[] getAffectedLinesInFile(Settings settings, Log log) throws Exception;
}
