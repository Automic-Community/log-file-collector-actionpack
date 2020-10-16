package com.uc4.ara.feature.logcollector.structure;

import com.uc4.ara.feature.utils.FileUtil;
import com.uc4.ara.util.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class Log {

	private ArrayList<LogEntry> logEntries;
	private String fileName;

	public Log(String fileName)
	{
		this.fileName = fileName;
		logEntries = new ArrayList<LogEntry>();
	}

	public ArrayList<LogEntry> getLogEntries()
	{
		return logEntries;
	}

	public String getFileName()
	{
		return fileName;
	}

	public int getEntryCount()
	{
		return this.getLogEntries().size();
	}

	public int getTotalLineCount()
	{
		return GetLinesOfFile(this.fileName);
	}

	public int getLineCount()
	{
		int count = 0;

		for (LogEntry entry : logEntries)
		{
			count += entry.getLineCount();
		}

		return count;
	}

	public String getEntryText()
	{
		StringBuilder builder = new StringBuilder();

		for (LogEntry entry : logEntries)
		{
			builder.append(entry.getEntryText());
		}

		return builder.toString();
	}

	public static int GetLinesOfFile(String fileName)
	{
		int count = 0;

		try {
			count = FileUtil.countLines(fileName);
		} catch (IOException e) {
			Logger.logException(e);
		}

		return count;
	}
}
