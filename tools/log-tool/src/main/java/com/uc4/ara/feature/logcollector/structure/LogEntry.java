package com.uc4.ara.feature.logcollector.structure;

import java.util.Date;

public class LogEntry {

	private String[] entries;
	private Date logDate;
	
	
	public String[] getEntries()
	{
		return entries;
	}
	
	public String getEntryText()
	{
		StringBuilder builder = new StringBuilder();
		
		for(String entry : entries)
		{
			builder.append(entry);
			builder.append("\r\n");
		}
		
		return builder.toString();
	}
	
	public Date getLogDate()
	{
		return logDate;
	}
	
	public int getLineCount()
	{
		return entries.length;
	}
	
	public LogEntry(String[] entries, Date logDate)
	{
		this.entries = entries; 
		this.logDate = logDate;
	}
}
