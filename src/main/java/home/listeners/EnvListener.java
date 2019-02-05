package home.listeners;

import org.apache.commons.compress.utils.IOUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import home.env.Env;
import home.env.EnvJson;
import home.env.EnvRegistry;
import home.env.ThreadEnv;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;

import static java.lang.System.getProperty;
import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

public class EnvListener extends TestListenerAdapter {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EnvListener.class);


    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        releaseEnv();
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        releaseEnv();
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        releaseEnv();
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        releaseEnv();
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        releaseEnv();
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        super.beforeConfiguration(tr);
        try {
            EnvRegistry.init();
        } catch (Exception e) {
            System.exit(3);
        }
        Env env;
        int i = 0;
        do {
            if (i==30) {
                Assert.fail("Нет свободных env");
            }
            logger.info("Try get free env");
            env = EnvRegistry.getFreeEnv();
            i++;
            try {
                if (env == null) Thread.sleep(2000);
            }
            catch (InterruptedException ignored)
            {
            }
        } while (env == null);
        ThreadEnv.setByThread(new ThreadEnv(env));
        i = 0;
    }

    private void releaseEnv() {
        if (!ThreadEnv.hasByThread()) return;
        Env env = ThreadEnv.getByThread().getEnv();
        ThreadEnv.removeByThread();
        EnvRegistry.releaseEnv(env);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        FileOutputStream fos = null;
        try {
            Properties props = new Properties();

            fos = new FileOutputStream("target/allure-results/environment.properties");
            Collection<EnvJson> collections = EnvRegistry.getCollection();
            int i = 0;
            for (EnvJson element:collections) {
                i++;
                ofNullable(props.setProperty("Окружение "+i,element.getUiURL()));
            }
            props.store(fos, "See https://github.com/allure-framework/allure-app/wiki/Environment");
            fos.close();
        } catch (IOException e) {
            logger.error("IO problem when writing allure properties file", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
}
