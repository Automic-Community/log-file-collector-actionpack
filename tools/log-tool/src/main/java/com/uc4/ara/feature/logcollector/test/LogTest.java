package com.uc4.ara.feature.logcollector.test;

import com.uc4.ara.feature.logcollector.structure.Log;
import com.uc4.ara.feature.logcollector.structure.LogEntry;
import com.uc4.ara.feature.utils.FileUtil;
import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * The class <code>LogTest</code> contains tests for the class {@link
 * <code>Log</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 19.06.13 11:21
 *
 * @author extsts
 *
 * @version $Revision$
 */
public class LogTest extends TestCase {



	private Log log;

	public LogTest(String name) {
		super(name);
	}

	/**
	 * Perform pre-test initialization
	 *
	 * @throws Exception
	 *
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		log = new Log("logtest.txt");

		ArrayList<String> entries = new ArrayList<String>();
		entries.add("Testline 1");
		entries.add("Testline 2");
		entries.add("Testline 3");
		entries.add("Testline 4");
		entries.add("");
		entries.add("Testline 5");

		LogEntry entry1 = new LogEntry(entries.toArray(new String[]{}), new Date());

		entries = new ArrayList<String>();
		entries.add("Testline 1");
		LogEntry entry2 = new LogEntry(entries.toArray(new String[]{}), new Date());

		entries = new ArrayList<String>();
		entries.add("Testline 1");
		entries.add("Testline 2");
		LogEntry entry3 = new LogEntry(entries.toArray(new String[]{}), new Date());

		log.getLogEntries().add(entry1);
		log.getLogEntries().add(entry2);
		log.getLogEntries().add(entry3);

		FileUtil.writeFile("logtest.txt", "Testline 1\r\nTestline 2\r\nTestline 3\r\nTestline 4\r\n\r\nTestline 5\r\nTestline 1\r\nTestline 1\r\nTestline 2\r\ninvalid");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		File file = new File("logtest.txt");
		file.delete();
	}

	/**
	 * Run the int getEntryCount() method test
	 */
	public void testGetEntryCount() {
		assertEquals(3, log.getEntryCount());
	}

	/**
	 * Run the String getEntryText() method test
	 */
	public void testGetEntryText() {
		String expectedText = "Testline 1\r\nTestline 2\r\nTestline 3\r\nTestline 4\r\n\r\nTestline 5\r\nTestline 1\r\nTestline 1\r\nTestline 2\r\n";
		assertEquals(expectedText, log.getEntryText());
	}

	/**
	 * Run the int getLineCount() method test
	 */
	public void testGetLineCount() {
		assertEquals(9, log.getLineCount());
	}

	/**
	 * Run the ArrayList<LogEntry> getLogEntries() method test
	 */
	public void testGetLogEntries() {
		ArrayList<LogEntry> entries = log.getLogEntries();

		assertEquals(3, entries.size());
		assertEquals("Testline 1\r\n", entries.get(1).getEntryText());
	}

	/**
	 * Run the int getTotalLineCount() method test
	 */
	public void testGetTotalLineCount() {
		assertEquals(10, log.getTotalLineCount());
	}
}
