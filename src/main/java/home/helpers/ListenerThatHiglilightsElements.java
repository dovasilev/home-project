package home.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;


public class ListenerThatHiglilightsElements extends AbstractWebDriverEventListener {

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        flash(element, driver);
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        flash(driver.findElement(by), driver);
    }

    public static void flash(WebElement element, WebDriver driver) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        changeColor(element, js);
    }

    public static void changeColor(WebElement element, JavascriptExecutor js) {
        js.executeScript("arguments[0].style.backgroundColor = 'grey'", element);
        js.executeScript("arguments[0].style.border='4px solid red'", element);
    }

}