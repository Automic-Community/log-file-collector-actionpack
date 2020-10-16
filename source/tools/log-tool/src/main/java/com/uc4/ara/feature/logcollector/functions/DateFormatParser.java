package com.uc4.ara.feature.logcollector.functions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatParser {

	private String[] dateFormats;
	
	public DateFormatParser(String datesCommaSeparated)
	{
		this.dateFormats = datesCommaSeparated.split(",");
		
		for(int i = 0; i < dateFormats.length; i++)
		{
			dateFormats[i] = dateFormats[i].trim();
		}
	}
	
	public String[] getDateFormats()
	{
		return this.dateFormats;
	}
	
	public boolean isValidDateFormat(String dateString)
	{
		boolean isValid = false;
		
		for(String dateFormat : this.dateFormats)
		{
			try 
			{
				SimpleDateFormat format = new SimpleDateFormat();
				format.setLenient(false);
				format.applyPattern(dateFormat);
				format.parse(dateString);
				
				isValid = true;
				break;
			} 
			catch (Exception e) 
			{
				//parsing unsuccessful
			}
		}
		
		return isValid;
	}
	
	public Date getDateFromString(String dateString)
	{
		Date parsedDate = new Date();
		
		for(String dateFormat : this.dateFormats)
		{
			try 
			{
				SimpleDateFormat format = new SimpleDateFormat();
				format.setLenient(false);
				format.applyPattern(dateFormat);
			
				parsedDate = format.parse(dateString);
				break;
			} 
			catch (Exception e) 
			{
				//parsing unsuccessful
			}
		}
		
		return parsedDate;
	}
}
