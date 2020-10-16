package com.uc4.ara.feature.logcollector.structure;

import java.util.Date;

public class Report {

	private Log[] logs;
	private Info info;
	private Settings settings;
	
	public Report(Log[] logs, Info info, Settings settings)
	{
		this.logs = logs;
		this.info = info;
		this.settings = settings;
	}
	
	public Log[] getLogs()
	{
		return this.logs;
	}
	
	public Info getInfo()
	{
		return this.info;
	}
	
	public Settings getSettings()
	{
		return this.settings;
	}
	
	public String toString()
	{
		return toString(true);
	}
	
	public String toString(Boolean isFileLog)
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("=== Settings ===\r\n");
		builder.append("Base path: " + settings.getBasePath() + "\r\n");
		builder.append("Pattern: " + settings.getFileNamePattern() + "\r\n");
		builder.append("Host date: " + (new Date()).toString() + "\r\n");
		builder.append("Timeframe: " + settings.getHourFrame() + "h " + settings.getMinuteFrame() + "m " + settings.getSecondFrame() + "s\r\n");
		builder.append("Start to collect from: " + settings.getStartCollectingDate().toString() + "\r\n");
		builder.append("Filter: " + settings.getFilterString() + "\r\n");
		
		builder.append("\r\n");
		
		builder.append("=== Info ===\r\n");
		if(isFileLog)
			builder.append("Number of log files: " + info.getNumberOfFiles() + "\r\n");
		
		builder.append("Output path: " + info.getOutputPath() + "\r\n");
		
		builder.append("\r\n");
		
		builder.append("=== Collected logs ===\r\n");
		for(Log log : this.logs)
		{	
			if(isFileLog)
			{
				builder.append("path: " + log.getFileName() + "\r\n");
				builder.append("Lines total: " + log.getTotalLineCount() + "\r\n");
				builder.append("after filter: " + log.getLineCount() + "\r\n");
			}
			else
			{
				builder.append("number of entries: " + log.getEntryCount() + "\r\n");
				builder.append("number of lines in all entries: " + log.getLineCount() + "\r\n");
			}
			builder.append("log entries from this file\r\n");
			builder.append(log.getEntryText() + "\r\n");
		}
		
		return builder.toString();
	}
}
