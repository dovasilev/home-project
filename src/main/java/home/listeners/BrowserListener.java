package home.listeners;

import io.qameta.allure.Attachment;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import home.env.ThreadEnv;

import java.util.logging.Level;

public class BrowserListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult tr) {
        LogEntries entries = ThreadEnv.getByThread().getDriver().manage().logs().get(LogType.BROWSER);
        entries.filter(Level.SEVERE);
        if (!entries.getAll().isEmpty())
            errorBrowser(entries);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        LogEntries entries = ThreadEnv.getByThread().getDriver().manage().logs().get(LogType.BROWSER);
        entries.filter(Level.SEVERE);
        if (!entries.getAll().isEmpty())
            errorBrowser(entries);
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Error browser log", type = "text/plain")
    public String errorBrowser(LogEntries entries) {
            String error = "";
            for (LogEntry entry:entries){
                error = error + entry.toString() + "\n";
            }
            return error;
    }
}
