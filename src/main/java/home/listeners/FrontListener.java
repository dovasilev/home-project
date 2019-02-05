package home.listeners;

import org.testng.*;
import home.env.ThreadEnv;
import org.testng.ITestContext;

public class FrontListener extends TestListenerAdapter {

    private CIListener ciListener;
    private ScreenshotListener screenshotListener;
    private WebDriverListener webDriverListener;
    private LogCatchListener logCatchListener;
    private EnvListener envListener;
    private BrowserListener browserListener;

    public FrontListener() {
        ciListener = new CIListener();
        screenshotListener = new ScreenshotListener();
        webDriverListener = new WebDriverListener();
        logCatchListener = new LogCatchListener();
        envListener = new EnvListener();
        browserListener = new BrowserListener();
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        screenshotListener.onTestSuccess(tr);
        webDriverListener.onTestSuccess(tr);
        ciListener.onTestSuccess(tr);
        logCatchListener.onTestSuccess(tr);
        envListener.onTestSuccess(tr);
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        if (ThreadEnv.hasByThread()) {
            screenshotListener.onTestFailure(tr);
            browserListener.onTestFailure(tr);
            webDriverListener.onTestFailure(tr);
            ciListener.onTestFailure(tr);
            logCatchListener.onTestFailure(tr);
            envListener.onTestFailure(tr);
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        if (ThreadEnv.hasByThread()) {
            webDriverListener.onTestSkipped(tr);
            ciListener.onTestSkipped(tr);
            logCatchListener.onTestSkipped(tr);
            envListener.onTestSkipped(tr);
        }
    }

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        ciListener.onStart(testContext);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        ciListener.onFinish(testContext);
        envListener.onFinish(testContext);

    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        super.beforeConfiguration(tr);
        logCatchListener.beforeConfiguration(tr);
        envListener.beforeConfiguration(tr);
        ciListener.beforeConfiguration(tr);
    }

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        logCatchListener.onTestStart(result);
        ciListener.onTestStart(result);
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        ciListener.onConfigurationFailure(itr);
        webDriverListener.onConfigurationFailure(itr);
        envListener.onConfigurationFailure(itr);
        logCatchListener.onConfigurationFailure(itr);
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        ciListener.onConfigurationSkip(itr);
        webDriverListener.onConfigurationSkip(itr);
        envListener.onConfigurationSkip(itr);
        logCatchListener.onConfigurationSkip(itr);
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        super.onConfigurationSuccess(itr);
        ciListener.onConfigurationSuccess(itr);
        envListener.onConfigurationSuccess(itr);
    }


}
