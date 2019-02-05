package home.listeners;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import home.env.ThreadEnv;

public class CIListener extends TestListenerAdapter {

    private static final Logger logger = Logger.getLogger(CIListener.class);

    private String getSessionID() {
        if (!ThreadEnv.hasByThread()) {
            return "Not initialized";
        }
        WebDriver oWebDriver = ThreadEnv.driver();
        if (oWebDriver instanceof WrapsDriver) {
            oWebDriver = ((WrapsDriver) oWebDriver).getWrappedDriver();
        }

        RemoteWebDriver webDriver;
        if (oWebDriver instanceof RemoteWebDriver) {
            webDriver = (RemoteWebDriver) oWebDriver;
        } else {
            return "Local session";
        }
        if (webDriver.getSessionId() == null) {
            return "Not Session";
        } else {
            return webDriver.getSessionId().toString();
        }
    }

    private String getUniqueID(ITestContext ctx, ITestResult result) {
        String testName;
        if (ctx == null && result == null) {
            return "ThreadID: " + (Long.toString(Thread.currentThread().getId()));
        }
        if (result == null) {
            testName = ctx.getName();
        } else {
            testName = result.getMethod().getMethodName();
        }
        return "SessionID: " + getSessionID() + "; ThreadID: " + (Long.toString(Thread.currentThread().getId())) + "; TestName: " + testName;
    }

    private String getUniqueID(ITestContext ctx) {
        return getUniqueID(ctx, null);
    }

    private String getUniqueID(ITestResult result) {
        return getUniqueID(result.getTestContext(), result);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        logger.info(getUniqueID(tr));
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        logger.info(getUniqueID(tr));
        logger.error(tr.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        logger.info(getUniqueID(tr));
    }

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        logger.info("Total tests: " + LogCatchListener.getTestsTotal());
        logger.info("Enabled tests: " + LogCatchListener.getTestsEnabled());
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        logger.info(getUniqueID(testContext));
        String message = "Stat: " +
                "active " + LogCatchListener.getActiveCount() + "; " +
                "failed " + LogCatchListener.getFailureCount() + "; " +
                "skipped " + LogCatchListener.getSkippedCount() + "; " +
                "success: " + LogCatchListener.getSuccessCount() + "; " +
                "finished: " + LogCatchListener.getFinishedCount()+ " of "+LogCatchListener.getTestsEnabled();
        logger.info(message);
    }

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        logger.info(getUniqueID(result));
        String message = "Stat: " +
                "active " + LogCatchListener.getActiveCount() + "; " +
                "failed " + LogCatchListener.getFailureCount() + "; " +
                "skipped " + LogCatchListener.getSkippedCount() + "; " +
                "success: " + LogCatchListener.getSuccessCount() + "; " +
                "finished: " + LogCatchListener.getFinishedCount()+ " of "+LogCatchListener.getTestsEnabled();
        logger.info(message);
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        super.beforeConfiguration(tr);
        logger.info(getUniqueID(tr));
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        logger.info(getUniqueID(itr));
        logger.error(itr.getThrowable().getMessage());
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        logger.info(getUniqueID(itr));
    }

    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        super.onConfigurationSuccess(itr);
        logger.info(getUniqueID(itr));
    }
}
