package home.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.testng.Assert;

public class ListenerThatHiglilightsElements extends AbstractWebDriverEventListener {

    /**
     * Выделение элемента цветом перед нажатием на него
     * @param element web-элемент
     * @param driver драйвер браузера
     */
    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        flash(element, driver);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Выделение элемента цветом после нахождения
     * @param by локатор элемента
     * @param element web-элемент
     * @param driver драйвер браузера
     */
    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        flash(driver.findElement(by), driver);
    }


    public void flash(WebElement element, WebDriver driver) {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        changeColor(element, js);
    }

    public void changeColor(WebElement element, JavascriptExecutor js) {
        js.executeScript("arguments[0].style.backgroundColor = 'grey'", element);
        js.executeScript("arguments[0].style.border='4px solid red'", element);
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        try
        {
            Alert alert = driver.switchTo().alert();
            String errorMessage = alert.getText();
            alert.dismiss();
            Assert.fail("Незапланированный alert: "+errorMessage);
        }
        catch (Exception e) {
        }
    }
}