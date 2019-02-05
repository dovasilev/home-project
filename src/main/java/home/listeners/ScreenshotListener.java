package home.listeners;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import home.env.ThreadEnv;

public class ScreenshotListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult failingTest) {
       // if (isAlive(ThreadEnv.getByThread().getDriver()))
        ThreadEnv.screnshoter().embedScreenshotFullPage("fail_" + failingTest.getName());
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
       // if (isAlive(ThreadEnv.getByThread().getDriver()))
        ThreadEnv.screnshoter().embedScreenshotFullPage("ok_" + testResult.getName());
    }


}