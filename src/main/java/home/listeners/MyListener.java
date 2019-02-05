package home.listeners;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class MyListener extends TestListenerAdapter {

    private ScreenshotListener screenshotListener;
    private WebDriverQuitListener webDriverQuitListener;
    private LogListener logListener;
    private EnvListener envListener;

    public MyListener() {
        screenshotListener = new ScreenshotListener();
        webDriverQuitListener = new WebDriverQuitListener();
        logListener = new LogListener();
        envListener = new EnvListener();
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        screenshotListener.onTestSuccess(tr);
        webDriverQuitListener.onTestSuccess(tr);
        logListener.onTestSuccess(tr);
        envListener.onTestSuccess(tr);
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        screenshotListener.onTestFailure(tr);
        webDriverQuitListener.onTestFailure(tr);
        logListener.onTestFailure(tr);
        envListener.onTestFailure(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        webDriverQuitListener.onTestSkipped(tr);
        logListener.onTestSkipped(tr);
        envListener.onTestSkipped(tr);
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        super.beforeConfiguration(tr);
        logListener.beforeConfiguration(tr);
        envListener.beforeConfiguration(tr);
    }

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        logListener.onTestStart(result);
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        webDriverQuitListener.onConfigurationFailure(itr);
        envListener.onConfigurationFailure(itr);
        logListener.onConfigurationFailure(itr);
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        webDriverQuitListener.onConfigurationSkip(itr);
        envListener.onConfigurationSkip(itr);
        logListener.onConfigurationSkip(itr);
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        super.onConfigurationSuccess(itr);
        envListener.onConfigurationSuccess(itr);
    }
}
