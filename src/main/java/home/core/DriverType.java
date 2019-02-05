package home.core;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;

import static org.openqa.selenium.remote.CapabilityType.PROXY;

public enum DriverType implements DriverSetup {

    FIREFOX {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            //System.setProperty("webdriver.firefox.bin", "C:\\tools\\Firefox\\firefox.exe");
            System.setProperty("webdriver.gecko.driver", "C:\\dist\\selenium\\geckodriver.exe");
            FirefoxProfile profile = new FirefoxProfile();

            //Set Location to store files after downloading.
            profile.setPreference("browser.download.dir", "D:\\WebDriverDownloads");
            profile.setPreference("browser.download.folderList", 2);

            //Set Preference to not show file download confirmation dialogue using MIME types Of different file extension types.
            profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;");

            profile.setPreference( "browser.download.manager.showWhenStarting", false );
            profile.setPreference( "pdfjs.disabled", true );

            //Pass FProfile parameter In webdriver to use preferences to download file.
            return new FirefoxDriver((Capabilities) profile);
            //return new FirefoxDriver(dr);
        }
    },
    CHROME {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("something", true);
            HashMap<String, Object> images = new HashMap<>();
            images.put("images", 2);

            HashMap<String, Object> chromePreferences = new HashMap<>();
            chromePreferences.put("profile.password_manager_enabled", "false");
            chromePreferences.put("profile.default_content_setting_values", images);

            ChromeOptions options = new ChromeOptions();
            options.addArguments("no-default-browser-check");
            options.addArguments("disable-infobars");
            options.addArguments("disable-extensions");
            options.addArguments("disable-web-security");
            options.addArguments("disable-gpu");
            options.addArguments("use-fake-device-for-media-stream");
            options.addArguments("use-fake-ui-for-media-stream");
            options.addArguments("test-type");
            options.addArguments("no-sandbox");
            options.addArguments("enable-strict-powerful-feature-restrictions");
            options.addArguments("browser.download.folderList", "2");
            options.addArguments("browser.download.dir", "/temp");
            options.addArguments("browser.download.useDownloadDir", "true");
            options.addArguments("browser.helperApps.neverAsk.saveToDisk", "application/pdf");
            options.addArguments("pdfjs.disabled", "true");
            options.setExperimentalOption("prefs", chromePreferences);
            options.addArguments("start-maximized");
            if (proxySettings == null) {
                options.addArguments("no-proxy-server");
            }

            capabilities.setCapability(ChromeOptions.CAPABILITY,options);
            capabilities.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "normal");

            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            System.setProperty("webdriver.chrome.driver", "C:\\dist\\selenium\\chromedriver.exe");
            return new ChromeDriver(capabilities);
        }
    },
    IE {
        public DesiredCapabilities getDesiredCapabilities(Proxy proxySettings) {
            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            capabilities.setCapability("requireWindowFocus", true);
            return addProxySettings(capabilities, proxySettings);
        }

        public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            return new InternetExplorerDriver(capabilities);
        }
    };

    protected DesiredCapabilities addProxySettings(DesiredCapabilities capabilities, Proxy proxySettings) {
        if (proxySettings == null) {
            proxySettings = new Proxy();
            proxySettings.setAutodetect(false);
            proxySettings.setProxyType(Proxy.ProxyType.DIRECT);
        }
        capabilities.setCapability(PROXY, proxySettings);

        return capabilities;
    }

}