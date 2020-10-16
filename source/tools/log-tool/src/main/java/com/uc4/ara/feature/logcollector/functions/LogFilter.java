package com.uc4.ara.feature.logcollector.functions;

public class LogFilter {

	public static boolean applyLineFilter(int lineIndex, int lineCount, boolean topDownLineCount, int totalLineCount) 
	{
		boolean isValid = false;
		
		if(lineCount == 0)
			isValid = true;
		else
		{
			if(topDownLineCount)
			{
				if(lineIndex < lineCount)
					isValid = true;
				else 
					isValid = false;
			} else {
	
				int calculatedLineLimit = (totalLineCount - lineCount);
				if(calculatedLineLimit < 0)
					calculatedLineLimit = 0;
				
				if(lineIndex < totalLineCount && lineIndex > calculatedLineLimit)
					isValid = true;
				else
					isValid = false;
			}
		}
		return isValid;
	}

}
