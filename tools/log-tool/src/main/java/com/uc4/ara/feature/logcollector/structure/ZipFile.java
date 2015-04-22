package com.uc4.ara.feature.logcollector.structure;

import com.uc4.ara.feature.utils.ZipUtil;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class ZipFile {
	private Report report;
	private String zipFilePath;

	public ZipFile(String zipFilePath, Report report)
	{
		this.zipFilePath = zipFilePath;
		this.report = report;
	}

	public Report getReport()
	{
		return this.report;
	}

	public String getZipFilePath()
	{
		return this.zipFilePath;
	}


	public void saveReportToZipFile() throws Exception
	{
		saveReportToZipFile(true);
	}

	public void saveReportToZipFile(Boolean isFileReport) throws Exception
	{
		File zipFile = new File(zipFilePath);
		String reportFileName = UUID.randomUUID().toString() + ".txt";
		String tempPath = zipFile.getAbsoluteFile().getParentFile().getAbsolutePath();
		File reportFile = new File(tempPath, reportFileName);

		FileWriter writer = new FileWriter(reportFile);
		writer.write(report.toString(isFileReport));
		writer.close();

		ZipUtil.zipFile(reportFile, zipFile);

		reportFile.delete();
	}
}
