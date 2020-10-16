package com.uc4.ara.feature.logcollector.test;

import com.uc4.ara.feature.logcollector.functions.LogFilter;
import junit.framework.TestCase;

/**
 * The class <code>LogFilterTest</code> contains tests for the class {@link
 * <code>LogFilter</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 24.06.13 11:30
 *
 * @author extsts
 *
 * @version $Revision$
 */
public class LogFilterTest extends TestCase {

	/**
	 * Construct new test instance
	 *
	 * @param name the test name
	 */
	public LogFilterTest(String name) {
		super(name);
	}
	
	public void testapplyLineFilterTopDownValid()
	{
		assertTrue(LogFilter.applyLineFilter(3, 100, true, 500));
	}
	
	public void testapplyLineFilterTopDownInValid()
	{
		assertTrue(!LogFilter.applyLineFilter(103, 100, true, 500));
	}
	
	public void testapplyLineFilterBottomUpValid()
	{
		assertTrue(LogFilter.applyLineFilter(403, 100, false, 500));
	}
	
	public void testapplyLineFilterBottomUpInValid()
	{
		assertTrue(!LogFilter.applyLineFilter(3, 100, false, 500));
	}
	
	public void testapplyLineFilterBottomUpLimitHigherThanTotalLines()
	{
		assertTrue(LogFilter.applyLineFilter(3, 600, false, 500));
	}
	
	public void testapplyLineFilterBottomUpValidLimitHigherThanTotalLines()
	{
		assertTrue(LogFilter.applyLineFilter(3, 600, false, 500));
	}
}
