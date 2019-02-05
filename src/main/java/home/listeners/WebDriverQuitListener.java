package home.listeners;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import home.env.ThreadEnv;

public class WebDriverQuitListener extends TestListenerAdapter {
    private static final Logger logger = Logger.getLogger(WebDriverQuitListener.class);

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
        if (ThreadEnv.driver() instanceof RemoteWebDriver) {
            logger.info("Close session " + ((RemoteWebDriver) ThreadEnv.driver()).getSessionId());
        } else if (ThreadEnv.driver() != null) {
            logger.info("Убиваю драйвер");
        } else {
            logger.info("Driver not instanced");
            return;
        }
        try {
            ThreadEnv.driver().quit();
        } catch (Exception ignored) {
        }
    }
}
