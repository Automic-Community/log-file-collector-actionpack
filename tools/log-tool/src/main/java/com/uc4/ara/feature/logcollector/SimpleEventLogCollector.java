package com.uc4.ara.feature.logcollector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.hyperic.sigar.win32.EventLog;
import org.hyperic.sigar.win32.EventLogRecord;
import com.uc4.ara.feature.AbstractInternalFeature;
import com.uc4.ara.feature.globalcodes.ErrorCodes;
import com.uc4.ara.feature.logcollector.structure.Info;
import com.uc4.ara.feature.logcollector.structure.Log;
import com.uc4.ara.feature.logcollector.structure.LogEntry;
import com.uc4.ara.feature.logcollector.structure.Report;
import com.uc4.ara.feature.logcollector.structure.Settings;
import com.uc4.ara.feature.logcollector.structure.ZipFile;
import com.uc4.ara.feature.utils.CmdLineParser;
import com.uc4.ara.util.Logger;

public class SimpleEventLogCollector extends AbstractInternalFeature {

	private ArrayList<String> filterApplications = new ArrayList<String>() ;

	private CmdLineParser.Option<String> outputPath;
	private CmdLineParser.Option<String> basePath;
	private CmdLineParser.Option<String> logPattern;
	private CmdLineParser.Option<String> zipFilePath;
	private CmdLineParser.Option<String> hourFrame;
	private CmdLineParser.Option<String> minuteFrame;
	private CmdLineParser.Option<String> secondFrame;
	private CmdLineParser.Option<String> lineCount;
	private CmdLineParser.Option<Boolean> isTopDown;

	@Override
	public void initialize()
	{
		super.initialize();

		outputPath = parser.addHelp(parser.addStringOption("o", "outputPath", true), "the output path");
		basePath = parser.addHelp(parser.addStringOption("b", "basePath", true), "the base path");
		logPattern = parser.addHelp(parser.addStringOption("p", "logPattern", true), "the pattern for log files");
		zipFilePath = parser.addHelp(parser.addStringOption("z", "zipFilePath", true), "the output zip file");
		hourFrame = parser.addHelp(parser.addStringOption("h", "hourFrame", true), "the timeframe in hour");
		minuteFrame = parser.addHelp(parser.addStringOption("m", "minuteFrame", true), "the timeframe in minute");
		secondFrame = parser.addHelp(parser.addStringOption("s", "secondFrame", true), "the timeframe in second");
		lineCount = parser.addHelp(parser.addStringOption("l", "lineCount", true), "the number of lines to collect from log files");
		isTopDown = parser.addHelp(parser.addBooleanOption("td", "isTopDown", false), "the direction of log collector within log files");
	}

	@Override
	public int run(String[] args) throws Exception
	{
		initialize();

		super.run(args);
		String outputPathValue = parser.getOptionValue(outputPath);
		String basePathValue = parser.getOptionValue(basePath);
		String logPatternValue = parser.getOptionValue(logPattern);
		String zipFilePathValue = parser.getOptionValue(zipFilePath);
		int hourFrameValue = Integer.valueOf(parser.getOptionValue(hourFrame));
		int minuteFrameValue = Integer.valueOf(parser.getOptionValue(minuteFrame));
		int secondFrameValue = Integer.valueOf(parser.getOptionValue(secondFrame));
		int lineCountValue = Integer.valueOf(parser.getOptionValue(lineCount));
		boolean isTopDownValue = parser.getOptionValue(isTopDown) == null ? false :parser.getOptionValue(isTopDown);

		Info info = new Info();
		info.setOutputPath(outputPathValue);

		Settings settings = new Settings(basePathValue, logPatternValue, hourFrameValue, minuteFrameValue, secondFrameValue, lineCountValue, isTopDownValue);
		ArrayList<Log> logs = new ArrayList<Log>();

		//find files
		ArrayList<LogEntry> entries = getLogEntriesInEventLog(settings);

		Log log = new Log("Application");
		log.getLogEntries().addAll(entries);
		logs.add(log);

		Report report = new Report(logs.toArray(new Log[]{}), info, settings);
		Logger.log(report.toString(false), loglevelValue);

		File outputDirectory = new File(outputPathValue);
		if(!outputDirectory.exists()) outputDirectory.mkdirs();
		ZipFile zipFile = new ZipFile(outputPathValue + File.separator + zipFilePathValue, report);
		zipFile.saveReportToZipFile(false);

		return ErrorCodes.OK;
	}

	public ArrayList<LogEntry> getLogEntriesInEventLog(Settings settings)
			throws Exception {

		unpackNativeLibs();

		ArrayList<LogEntry> entries = new ArrayList<LogEntry>();

		EventLog log = new EventLog();
		try {
			log.open(settings.getBasePath());
		} catch(UnsatisfiedLinkError err) {
			Logger.log("Error: No native libraries found for this machine. Job exiting..", loglevelValue);
			return new ArrayList<LogEntry>();
		}

		filterApplications = initializeAllowedSources(settings.getFileNamePattern());

		int max = log.getNumberOfRecords();

		int last = log.getNewestRecord()+1;
		int first = last - max;

		for (int i=first; i<last; i++) {
			try {
				EventLogRecord record = log.read(i);
				if(filterApplications.contains(record.getSource()))
				{
					if(record.getTimeGenerated() > (System.currentTimeMillis() / 1000 - settings.getTotalFrameInSeconds()))
					{
						LogEntry entry = new LogEntry(new String[] {new String(record.toString())}, new Date(record.getTimeGenerated()));
						entries.add(entry);
					}
				}
			} catch (Exception e) { Logger.log("Error occurred while reading entry " + i + ": " + e.getMessage() + ". Skip this entry.", loglevelValue); }
		}

		return entries;
	}

	public ArrayList<String> initializeAllowedSources(String fileNamePattern) {
		ArrayList<String> allowedSources = new ArrayList<String>();

		for(String pattern : fileNamePattern.split(","))
		{
			allowedSources.add(pattern);
		}

		return allowedSources;
	}

	/**
	 * Unpacks libraries for sigar to current working directory
	 * @throws IOException
	 */
	public void unpackNativeLibs() {
		String[] libs = new String[] {"sigar-x86-winnt.lib", "sigar-x86-winnt.dll", "sigar-amd64-winnt.dll"};
		for (String lib : libs) {
			try {
				String output = System.getProperty("user.dir") + System.getProperty("file.separator") + lib;
				if (new File(output).isFile()) {
					continue;
				}
				InputStream in = SimpleEventLogCollector.class.getResourceAsStream("/lib/" + lib);
				byte[] buffer = new byte[8192];
				int read = -1;

				FileOutputStream fos = new FileOutputStream(output);

				while((read = in.read(buffer)) != -1) {
					fos.write(buffer, 0, read);
				}
				fos.close();
				in.close();

				//System.load(output);
			} catch (Exception e) {
				Logger.log("Error occurred when unpacking Sigar native libraries. Error message: " + e.getMessage(), loglevelValue);
			}
		}
	}

}
