package home.listeners;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import home.core.DriverFactory;
import home.env.ThreadEnv;

public class WebDriverListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        closeAll();
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        closeAll();
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        closeAll();
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        closeAll();
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        closeAll();
    }

    private void closeAll() {
        DriverFactory.releaseDriver(ThreadEnv.env(), ThreadEnv.driver());
    }

}
