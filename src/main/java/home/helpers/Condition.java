package home.helpers;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import home.env.Env;


public class Condition {
    private static final Logger logger = Logger.getLogger(Condition.class);
    public WebDriver driver;

    public Condition(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement isDisplayed(By by) {
        logger.info("Ожидаю элемента в течении " + Env.WAIT_ELEMENT + " секунд");
        WebDriverWait wait = new WebDriverWait(driver, Env.WAIT_ELEMENT);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void waitUntilElementPresent(WebElement element, int time) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, time);
            wait.until(ExpectedConditions.stalenessOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Время ожидания исчезновения элемента истекло");
        }
    }
}
