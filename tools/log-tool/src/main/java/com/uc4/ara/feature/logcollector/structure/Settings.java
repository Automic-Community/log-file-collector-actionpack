package com.uc4.ara.feature.logcollector.structure;

import java.util.Date;

public class Settings {

	private String basePath;
	private String fileNamePattern;
	private int hourFrame;
	private int minuteFrame;
	private int secondFrame;
	private int lineCount;
	private boolean topDownLineCount;
	
	private Date startCollectingDate;
	
	public Settings(String basePath, String pattern, int hourFrame, int minuteFrame, int secondFrame, int lineCount, boolean topDownLineCount)
	{
		this.basePath = basePath;
		this.fileNamePattern = pattern;
		this.hourFrame = hourFrame;
		this.minuteFrame = minuteFrame;
		this.secondFrame = secondFrame;
		this.lineCount = lineCount;
		this.topDownLineCount = topDownLineCount;
		this.startCollectingDate = new Date();
	}
	
	public String getBasePath()
	{
		return this.basePath;
	}
	
	public String getFileNamePattern()
	{
		return this.fileNamePattern;
	}
	
	public int getHourFrame()
	{
		return this.hourFrame;
	}
	
	public int getMinuteFrame()
	{
		return this.minuteFrame;
	}
	
	public int getSecondFrame()
	{
		return this.secondFrame;
	}
	
	public int getLineCount()
	{
		return this.lineCount;
	}
	
	public boolean getTopDownLineCount()
	{
		return this.topDownLineCount;
	}
	
	public Date getStartCollectingDate()
	{
		return this.startCollectingDate;
	}
	
	public long getTotalFrameInSeconds()
	{
		return (long)(this.hourFrame * 3600) + this.minuteFrame * 60 + this.secondFrame;
	}

	public String getFilterString() 
	{
		StringBuilder filter = new StringBuilder();
		Date baseTime = new Date(getStartCollectingDate().getTime() - (getTotalFrameInSeconds() * 1000));
		filter.append("Time >= " + baseTime);
		
		if(getLineCount() > 0) 
		{
			if(getTopDownLineCount())
			{
				filter.append(" && First " + getLineCount() + " lines");
			}
			else 
			{
				filter.append(" && Last " + getLineCount() + " lines");
			}
		}
		
		return filter.toString();
	}
}
