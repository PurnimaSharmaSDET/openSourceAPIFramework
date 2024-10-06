package com.opensourceFramework.utils.extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.LinkedList;

/**
 * ExtentManager is responsible for managing the ExtentReports instance, handling
 * configurations, and providing utilities for logging, reporting, and maintaining test states.
 */
public class ExtentManager {

    private static ExtentKlovReporter klovReporter = null;
    private static ExtentReports extent = null;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static boolean removeRetriedTests;
    private static boolean addScreenshotsToReport;
    private static final Logger logger = LogManager.getLogger();
    private static ThreadLocal<LinkedList<String>> logTracking = new ThreadLocal<>();
    private static ThreadLocal<LinkedList<Markup>> extentTestBefore = new ThreadLocal<>();
    private static String extentReportLocation;
    private static final String OUTPUT_FOLDER_SCREENSHOTS = "screenshots/";
    private static String reportName = "AutomationSuiteReport.html";
    private static Boolean loggingEnabled = true;
    private static String loggerName = "LoggerFile";

    /**
     * Constructor for ExtentManager.
     */
    public ExtentManager() {
    }

    /**
     * Retrieves the thread-safe list of Markups logged before the test starts.
     *
     * @return ThreadLocal list of Markups.
     */
    public static ThreadLocal<LinkedList<Markup>> getExtentTestBefore() {
        return extentTestBefore;
    }

    /**
     * Adds a log entry (Markup) before the test begins.
     *
     * @param log The Markup log to be added.
     */
    public static void setExtentTestBefore(Markup log) {
        if (extentTestBefore.get() == null) {
            LinkedList<Markup> logList = new LinkedList<>();
            getExtentTestBefore().set(logList);
        }
        getExtentTestBefore().get().add(log);
    }

    /**
     * Retrieves the loggingEnabled flag to determine if logging is enabled.
     *
     * @return Boolean flag indicating logging status.
     */
    public static Boolean getLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * Sets the loggingEnabled flag to enable or disable logging.
     *
     * @param loggingEnabled Boolean flag to set logging status.
     */
    public static void setLoggingEnabled(Boolean loggingEnabled) {
        ExtentManager.loggingEnabled = loggingEnabled;
    }

    /**
     * Retrieves the logger file name.
     *
     * @return Logger file name as a String.
     */
    public static String getLoggerName() {
        return loggerName;
    }

    /**
     * Sets the logger file name.
     *
     * @param loggerName Name of the logger file.
     */
    public static void setLoggerName(String loggerName) {
        ExtentManager.loggerName = loggerName;
    }

    /**
     * Retrieves the report name.
     *
     * @return Name of the report file.
     */
    public static String getReportName() {
        return reportName;
    }

    /**
     * Sets the report name.
     *
     * @param reportName Name of the report file.
     */
    public static void setReportName(String reportName) {
        ExtentManager.reportName = reportName;
    }

    /**
     * Retrieves the current ExtentTest instance, ensuring thread safety.
     *
     * @return ThreadLocal instance of ExtentTest.
     */
    public synchronized static ThreadLocal<ExtentTest> getTest() {
        return test;
    }

    /**
     * Retrieves the current logs for tracking in a thread-safe manner.
     *
     * @return ThreadLocal instance of log tracking list.
     */
    public synchronized static ThreadLocal<LinkedList<String>> getLog() {
        return logTracking;
    }

    /**
     * Retrieves the Logger instance for logging.
     *
     * @return Logger instance.
     */
    public synchronized static Logger getLogger() {
        return logger;
    }

    /**
     * Sets the current ExtentTest instance.
     *
     * @param test The ExtentTest instance to be set.
     */
    public synchronized static void setTest(ExtentTest test) {
        getTest().set(test);
    }

    /**
     * Initializes the log tracking list for the current thread.
     */
    public synchronized static void setLog() {
        LinkedList<String> logs = new LinkedList<>();
        getLog().set(logs);
    }

    /**
     * Retrieves the location where the ExtentReport will be generated.
     *
     * @return Path of the ExtentReport as a String.
     */
    public static String getExtentReportLocation() {
        return extentReportLocation;
    }

    /**
     * Sets the location for the ExtentReport file.
     *
     * @param extentReportLocation The location to store the report.
     */
    public static void setExtentReportLocation(String extentReportLocation) {
        ExtentManager.extentReportLocation = extentReportLocation;
    }

    /**
     * Creates an instance of ExtentReports, attaching reporters and applying configurations.
     *
     * @param documentTitle         Title of the document.
     * @param removeRetriedTests    Whether to remove retried tests from the report.
     * @param addScreenshotsToReport Whether to add screenshots to the report.
     * @param reportName            Name of the report file.
     * @return Instance of ExtentReports.
     */
    public synchronized static ExtentReports createInstance(String documentTitle, boolean removeRetriedTests, boolean addScreenshotsToReport, String reportName) {
        setReportName(reportName + ".html");
        setRemoveRetriedTests(removeRetriedTests);
        setAddScreenshotsToReport(addScreenshotsToReport);
        setExtentReportLocation(ServerReporterPath.getReportBaseDirectory() + getReportName());

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(getExtentReportLocation());
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setReportName(documentTitle);
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setTimeStampFormat("MM/dd/yyyy, hh:mm:ss a '('zzz')'");
        sparkReporter.viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG})
                .apply();

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        if (klovReporter != null)
            extent.attachReporter(klovReporter);

        return extent;
    }

    /**
     * Logs messages to Log4j if logging is enabled.
     */
    private static synchronized void writeLog4j() {
        if (getLoggingEnabled()) {
            if (logTracking.get() != null) {
                for (String message : logTracking.get()) {
                    logger.info(message);
                }
            }
        }
    }

    /**
     * Flushes the current ExtentReports data, writes logs, and cleans up resources.
     */
    public synchronized static void flush() {
        extent.flush();
        writeLog4j();
        if (getTest().get() != null) {
            getTest().remove();
        }
        if (getLog().get() != null) {
            getLog().remove();
        }
    }

    /**
     * Sets the pass percentage information in the report.
     *
     * @param value Pass percentage to be set.
     */
    public synchronized static void setPassPercentage(String value) {
        extent.setSystemInfo("Pass %", MarkupHelper.createLabel(value, ExtentColor.GREEN).getMarkup());
        flush();
    }

    /**
     * Adds system information to the ExtentReport.
     *
     * @param key   System info key.
     * @param value System info value.
     */
    public synchronized static void addSystemInfo(String key, String value) {
        extent.setSystemInfo(key, value);
        flush();
    }

    /**
     * Adds log output to the ExtentReport.
     *
     * @param log The log output.
     */
    public synchronized static void setTestRunnerOutput(String log) {
        extent.addTestRunnerOutput(log);
    }

    /**
     * Creates a test in the report with a name and description.
     *
     * @param testName    Name of the test.
     * @param description Description of the test.
     */
    public synchronized static void createTest(String testName, String description) {
        setTest(extent.createTest(testName, description));
        setLog();
        System.out.println("************" + testName + "************");
        logTracking.get().add("************" + testName + "************");

        if (getExtentTestBefore().get() != null && getExtentTestBefore().get().size() > 0) {
            for (Markup log : getExtentTestBefore().get()) {
                getTest().get().log(Status.INFO, log);
            }
        }
    }

    /**
     * Logs a message in the ExtentReport as well as the local log list for Log4j.
     *
     * @param status  Status of the log (e.g., PASS, FAIL).
     * @param message Log message.
     */
    public synchronized static void log(Status status, String message) {
        getTest().get().log(status, message);
        logTracking.get().add(message);
    }

    /**
     * Retrieves the removeRetriedTests flag.
     *
     * @return Boolean flag indicating whether retried tests are removed.
     */
    public static boolean isRemoveRetriedTests() {
        return removeRetriedTests;
    }

    /**
     * Sets the flag to remove retried tests.
     *
     * @param removeRetriedTests Boolean flag for removing retried tests.
     */
    public static void setRemoveRetriedTests(boolean removeRetriedTests) {
        ExtentManager.removeRetriedTests = removeRetriedTests;
    }

    /**
     * Retrieves the flag indicating whether screenshots are added to the report.
     *
     * @return Boolean flag for adding screenshots.
     */
    public static boolean isAddScreenshotsToReport() {
        return addScreenshotsToReport;
    }

    /**
     * Sets the flag to add screenshots to the report.
     *
     * @param addScreenshotsToReport Boolean flag for adding screenshots.
     */
    public static void setAddScreenshotsToReport(boolean addScreenshotsToReport) {
        ExtentManager.addScreenshotsToReport = addScreenshotsToReport;
    }
}
