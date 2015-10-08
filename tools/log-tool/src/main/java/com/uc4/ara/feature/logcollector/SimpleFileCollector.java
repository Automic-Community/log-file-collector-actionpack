package com.uc4.ara.feature.logcollector;

import com.uc4.ara.common.unicode.UnicodeInputStreamReader;
import com.uc4.ara.feature.AbstractInternalFeature;
import com.uc4.ara.feature.globalcodes.ErrorCodes;
import com.uc4.ara.feature.logcollector.functions.LogFilter;
import com.uc4.ara.feature.logcollector.structure.*;
import com.uc4.ara.feature.utils.CmdLineParser;
import com.uc4.ara.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;

public class SimpleFileCollector extends AbstractInternalFeature implements ILogCollector {

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
    public void initialize() {
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
        if(isValidDir(new File(settings.getBasePath()))) {
            String[] filesInPath = getFilesInPath(settings);

        //filter affected files in timeframe
        String[] affectedFiles = this.getAffectedFiles(settings, filesInPath);

        info.setNumberOfFiles(affectedFiles.length);

        //foreach file
        for(String fileName : affectedFiles)
        {
            //create log
            Log log = new Log(settings.getBasePath() + File.separator + fileName);
            logs.add(log);

            //find affected lines
            String[] lines = this.getAffectedLinesInFile(settings, log);
            LogEntry entry = new LogEntry(lines, new Date());

            log.getLogEntries().add(entry);
        }

        //if specified create report
        Report report = new Report(logs.toArray(new Log[]{}), info, settings);
        Logger.log(report.toString(), this.loglevelValue);

        //if specified create zipfile
        File outputDirectory = new File(outputPathValue);
        if(!outputDirectory.exists())
            outputDirectory.mkdirs();
        ZipFile zipFile = new ZipFile(outputPathValue + File.separator + zipFilePathValue, report);
        zipFile.saveReportToZipFile();

        return ErrorCodes.OK;
        } else {
            Logger.log("Directory " + settings.getBasePath() + " does not exist "
                + "OR Input value is a file OR You don't have access permission to this directory!", this.loglevelValue);
            return ErrorCodes.ERROR;
        }
    }

    @Override
    public String[] getFilesInPath(final Settings settings) {
        File directory = new File(settings.getBasePath());
        ArrayList<String> files = new ArrayList<String>();

        if(directory.exists())
        {
            for (String fileName : directory.list())
            {
                String[] patterns = settings.getFileNamePattern().split(",");

                boolean isValid = false;
                for(String pattern : patterns)
                {
                    isValid = fileName.matches(pattern);
                    if(isValid)
                    {
                        files.add(fileName);
                    }
                }
            }
        }
        return files.toArray(new String[]{});
    }

    @Override
    public String[] getAffectedFiles(Settings settings, String[] fileNames) {
        ArrayList<String> affectedFiles = new ArrayList<String>();

        for(String fileName : fileNames)
        {
            File file = new File(settings.getBasePath() + File.separator + fileName);
            if(file.lastModified() > (System.currentTimeMillis() - settings.getTotalFrameInSeconds() * 1000))
                affectedFiles.add(fileName);
        }

        return affectedFiles.toArray(new String[]{});
    }

    @Override
    public String[] getAffectedLinesInFile(Settings settings, Log log) {
        ArrayList<String> affectedLines = new ArrayList<String>();

        BufferedReader reader = null;
        RandomAccessFile raf = null;

        UnicodeInputStreamReader isr = null;

        try
        {
            File file = new File(log.getFileName());
            raf = new RandomAccessFile(file, "r");

            raf.seek(0L);
            isr = new UnicodeInputStreamReader(raf.getFD(), "");

            reader = new BufferedReader(isr);

            String line = "";
            int totalLineCount = log.getTotalLineCount();
            int i = 0;
            while((line = reader.readLine()) != null)
            {
                //check if line matches date
                //check if line matches lines-filter
                if(LogFilter.applyLineFilter(i, settings.getLineCount(), settings.getTopDownLineCount(), totalLineCount))
                {
                    affectedLines.add(line);
                }
                else if(settings.getTopDownLineCount())
                {
                    break;
                }
                i++;
            }
        }
        catch(IOException ioex)
        {
            Logger.logException(ioex);
        }
        finally
        {
            if (raf != null) {
                try
                {
                    raf.close();
                }
                catch(IOException ioex2)
                {
                    Logger.logException(ioex2);
                }
            }

            if(reader != null)
            {
                try
                {
                    reader.close();
                }
                catch(IOException ioex2)
                {
                    Logger.logException(ioex2);
                }
            }
        }

        return affectedLines.toArray(new String[]{});
    }

    private boolean isValidDir(File directory) {
        return directory.exists() && directory.isDirectory();
    }
}
