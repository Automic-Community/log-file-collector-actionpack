package com.uc4.ara.feature.logcollector.test;

import com.uc4.ara.feature.logcollector.structure.*;
import junit.framework.TestCase;

import java.io.File;

/**
 * The class <code>ZipFileTest</code> contains tests for the class {@link
 * <code>ZipFile</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 20.06.13 11:06
 *
 * @author extsts
 *
 * @version $Revision$
 */
public class ZipFileTest extends TestCase {

	private ZipFile zipFile;
	private final String zipFileName =  "mytestzip.zip";

	/**
	 * Construct new test instance
	 *
	 * @param name the test name
	 */
	public ZipFileTest(String name) {
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

		Log[] logs = new Log[1];
		Log log = new Log("testlog.txt");
		logs[0] = log;

		Info info = new Info();
		info.setOutputPath("myoutputPath");
		info.setNumberOfFiles(1);

		Settings settings = new Settings(".", ".*.txt", 1, 5, 45, 0, false);

		Report report = new Report(logs, info, settings);

		zipFile = new ZipFile(zipFileName, report);
	}

	/**
	 * Perform post-test clean up
	 *
	 * @throws Exception
	 *
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		// Add additional tear down code here
	}



	/**
	 * Run the void saveReportToZipFile() method test
	 * @throws Exception
	 */
	public void testSaveReportToZipFile() throws Exception {
		zipFile.saveReportToZipFile();

		File file = new File(zipFileName);
		assertTrue(file.exists());
	}
}
