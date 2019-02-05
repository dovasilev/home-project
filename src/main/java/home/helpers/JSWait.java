package home.helpers;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import home.env.Env;

import java.util.concurrent.TimeUnit;

public class JSWait {
    private WebDriver driver;
    public static final Logger logger = Logger.getLogger(JSWait.class);

    public JSWait (WebDriver webDriver) {
        this.driver=webDriver;
    }

    public synchronized void waitForAjax() throws InterruptedException {
        String action="Ajax_call";
        driver.manage().timeouts().setScriptTimeout(Env.WAIT_ELEMENT*2, TimeUnit.SECONDS);
        logger.info("Ожидаю загрузки элементов");
        ((JavascriptExecutor) driver).executeAsyncScript(
                "var callback = arguments[arguments.length - 1];" +
                        "var xhr = new XMLHttpRequest();" +
                        "xhr.open('GET', '/" + action + "', true);" +
                        "xhr.onreadystatechange = function() {" +
                        "  if (xhr.readyState == 4) {" +
                        "    callback(xhr.responseText);" +
                        "  }" +
                        "};" +
                        "xhr.send();");
    }

    public synchronized void sleep(int timeout) throws InterruptedException {
        //driver.wait(timeout);
        Thread.sleep(timeout);
    }
}
