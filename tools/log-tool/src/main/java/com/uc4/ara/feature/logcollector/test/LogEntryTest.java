package com.uc4.ara.feature.logcollector.test;

import com.uc4.ara.feature.logcollector.structure.LogEntry;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;

/**
 * The class <code>LogEntryTest</code> contains tests for the class {@link
 * <code>LogEntry</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 19.06.13 11:12
 *
 * @author extsts
 *
 * @version $Revision$
 */
public class LogEntryTest extends TestCase {


	private LogEntry myEntry;

	public LogEntryTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception
	{
		super.setUp();
		ArrayList<String> entries = new ArrayList<String>();
		entries.add("Testline 1");
		entries.add("Testline 2");
		entries.add("Testline 3");
		entries.add("Testline 4");
		entries.add("");
		entries.add("Testline 5");

		myEntry = new LogEntry(entries.toArray(new String[]{}), new Date());


	}

	/**
	 * Run the String getEntryText() method test
	 */
	public void testGetEntryText() {
		String expectedText = "Testline 1\r\nTestline 2\r\nTestline 3\r\nTestline 4\r\n\r\nTestline 5\r\n";
		assertEquals(expectedText, myEntry.getEntryText());
	}

	/**
	 * Run the int getLineCount() method test
	 */
	public void testGetLineCount() {
		assertEquals(6, myEntry.getLineCount());
	}
}
