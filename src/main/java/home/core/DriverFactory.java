package home.core;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import home.env.Env;

import java.net.URL;
import java.util.HashMap;

public class DriverFactory {
    public static boolean useWebDriverCache = false;
    public static boolean needSleepAfterCreateDriver = false;
    private static final Logger logger = Logger.getLogger(DriverFactory.class);
    private static HashMap<String, WebDriver> driversCache = new HashMap<>();
    private static DriverType defaultDriverType = DriverType.CHROME;
    private static String browser = System.getProperty("browser", defaultDriverType.toString()).toUpperCase();

    @Override
    protected void finalize() {
        for (WebDriver driver : driversCache.values()) {
            try {
                driver.quit();
            } catch (Exception ignored) {
            }
        }
        driversCache.clear();
    }

    public synchronized static WebDriver newDriver(Env env) {
        if (useWebDriverCache && driversCache.containsKey(env.getSdURL())) {
            WebDriver webDriver = driversCache.remove(env.getSdURL());
            logger.info("Reuse exists driver");
            return webDriver;
        }

        logger.info("Create new web driver");
        DriverType driverType = determineEffectiveDriverType();
        DesiredCapabilities desiredCapabilities = driverType.getDesiredCapabilities(null);
        WebDriver webdriver = instantiateWebDriver(driverType, env, desiredCapabilities);
        EventFiringWebDriver driver = new EventFiringWebDriver(webdriver);
        ListenerThatHiglilightsElements listener = new ListenerThatHiglilightsElements();
        driver.register(listener);


        if (needSleepAfterCreateDriver) {
            try {
                Thread.sleep(Env.DRIVER_SLEEP);
            } catch (InterruptedException ignored) {
            }
        }

        return driver;
    }

    public synchronized static void releaseDriver(Env env, WebDriver driver) {
        if (useWebDriverCache && !driversCache.containsKey(env.getSdURL())) {
            logger.info("Session was saved for reuse");
            driversCache.put(env.getSdURL(), driver);
            return;
        }

        if (driver instanceof WrapsDriver && ((WrapsDriver) driver).getWrappedDriver() instanceof RemoteWebDriver) {
            logger.info("Close session " + ((RemoteWebDriver) ((WrapsDriver) driver).getWrappedDriver()).getSessionId());
        } else if (driver != null) {
            logger.info("Close current session");
        } else {
            logger.info("Driver not instanced");
            return;
        }

        try {
            driver.quit();
        } catch (Exception ignored) {
        }
    }


    private static DriverType determineEffectiveDriverType() {
        DriverType driverType = defaultDriverType;
        try {
            driverType = DriverType.valueOf(browser);
            logger.info(driverType);
        } catch (IllegalArgumentException ignored) {
            logger.error("Unknown driver specified, defaulting to '" + driverType + "'...");
        } catch (NullPointerException ignored) {
            logger.error("No driver specified, defaulting to '" + driverType + "'...");
        }
        return driverType;
    }

    private static WebDriver instantiateWebDriver(DriverType driverType, Env env, DesiredCapabilities desiredCapabilities) {
        if (env.getSdURL() != null) {
            if (!env.getSdURL().equals("")) {
                try {
                    logger.info(env.getSdURL());
                    desiredCapabilities.setCapability("enableVNC", true);
                    RemoteWebDriver webDriver = new RemoteWebDriver(new URL(env.getSdURL()), desiredCapabilities);
                    logger.info("Created new driver with session id is " + webDriver.getSessionId().toString());
                    return webDriver;
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    e.getMessage();
                }
            }
        } else {
            return driverType.getWebDriverObject(desiredCapabilities);
        }
        throw new RuntimeException("");
    }

}

