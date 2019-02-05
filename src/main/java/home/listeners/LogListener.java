package home.listeners;

import home.helpers.MultithreadedConsoleOutputCatcher;
import io.qameta.allure.Attachment;
import io.qameta.allure.TmsLink;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.Test;
import org.testng.internal.ClassHelper;
import org.testng.internal.PackageUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class LogListener extends TestListenerAdapter {

    private static final Logger logger = Logger.getLogger(LogListener.class);
    private static int counter = 1;

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        logger.info("Total tests: " + LogListener.getTestsTotal());
        logger.info("Enabled tests: " + LogListener.getTestsEnabled());
    }

    @Override
    public void onTestFailure(ITestResult failingTest) {
        logger.error("Тест " + failingTest.getName() + " упал");
        logger.error("Fail message: " + failingTest.getThrowable().getMessage());
        logger.info("-----------------------------------------------------------------------------------------------------");
        failingTest.getThrowable().printStackTrace();
        stopCatch();
    }

    @Override
    public void onConfigurationSkip(ITestResult itr) {
        super.onConfigurationSkip(itr);
        stopCatch();
    }

    @Override
    public void onConfigurationFailure(ITestResult itr) {
        super.onConfigurationFailure(itr);
        logger.error("Configuration fail message: " + itr.getThrowable().getMessage());
        logger.info("-----------------------------------------------------------------------------------------------------");
        itr.getThrowable().printStackTrace();
        stopCatch();
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        logger.info("Тест " + testResult.getName() + " прошел успешно");
        logger.info("-----------------------------------------------------------------------------------------------------");
        stopCatch();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Тест " + result.getName() + " пропущен");
        logger.info("-----------------------------------------------------------------------------------------------------");
        stopCatch();
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        super.beforeConfiguration(tr);
        MultithreadedConsoleOutputCatcher.startCatch();
    }

    @Override
    public void onTestStart(ITestResult testResult) {
        logger.info("-----------------------------------------------------------------------------------------------------");
        logger.info("Тест " + testResult.getName() + " с номером теста " + testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TmsLink.class).value() + " начался");
        logger.info("Выполняю " + counter + " тест из " + LogListener.getTestsEnabled());
        counter++;
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "Test Log", type = "text/plain")
    public String stopCatch() {
        String result = MultithreadedConsoleOutputCatcher.getContent();
        MultithreadedConsoleOutputCatcher.stopCatch();
        return result;
    }

    public static int getCurrentTest() {
        return counter;
    }

    private static int testsEnabled = -1;

    public static int getTestsEnabled() {
        if (testsEnabled > 0) {
            return testsEnabled;
        }
        try {
            String[] testClasses = PackageUtils.findClassesInPackage("home.tests.*", new ArrayList<String>(), new ArrayList<String>());
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            testsEnabled = 0;
            for (String eachClass : testClasses) {
                Class currentClass = cl.loadClass(eachClass);
                Set<Method> allMethods = ClassHelper.getAvailableMethods(currentClass);
                for (Method eachMethod : allMethods) {
                    Test test = eachMethod.getAnnotation(Test.class);
                    if (test != null && test.enabled()) {
                        testsEnabled = testsEnabled + 1;
                    }
                }
            }
            return testsEnabled;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int testsTotal = -1;

    public static int getTestsTotal() {
        if (testsTotal > 0) {
            return testsTotal;
        }
        try {
            String[] testClasses = PackageUtils.findClassesInPackage("ru.ibc.tests.*", new ArrayList<String>(), new ArrayList<String>());
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            for (String eachClass : testClasses) {
                Class currentClass = cl.loadClass(eachClass);
                Set<Method> allMethods = ClassHelper.getAvailableMethods(currentClass);
                for (Method eachMethod : allMethods) {
                    Test test = eachMethod.getAnnotation(Test.class);
                    if (test != null) {
                        testsTotal++;
                    }
                }
            }
            return testsTotal;
        } catch (Exception e) {
            return 0;
        }
    }

}
