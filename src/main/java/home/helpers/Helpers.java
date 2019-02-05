package home.helpers;

import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import home.core.Screnshoter;
import home.env.Env;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Helpers {
    private static final Logger logger = Logger.getLogger(Helpers.class);
    public WebDriver driver;
    public Screnshoter screnshoter;

    public Helpers(WebDriver driver) {
        this.driver = driver;
        this.screnshoter = new Screnshoter(driver);
    }

    /**
     * Поиск элемента по имени атрибута и его значения
     * @param nameAttribute имя атрибута (например name)
     * @param content значения атрибута (например login)
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findElementByAttribute(String nameAttribute, String content) throws Exception {
        WebElement element;
        try {
            logger.info("Поиск элемента с атрибутом: " + nameAttribute + " и значением атрибута: " + content);
            element = driver.findElement(By.xpath("//*[contains(@" + nameAttribute + "," + "\"" + content + "\"" + ")]"));
        } catch (Exception e) {
            logger.error("Нет элемента с атрибутом: " + nameAttribute + " и значением: " + content);
            throw new Exception("Нет элемента с атрибутом: " + nameAttribute + " и значением: " + content);
        }
        return element;
    }

    public List<WebElement> findElementsByAttribute(String nameAttribute, String content) throws Exception {
        List<WebElement> element;
        try {
            logger.info("Поиск элемента с атрибутом: " + nameAttribute + " и значением атрибута: " + content);
            element = driver.findElements(By.xpath("//*[contains(@" + nameAttribute + "," + "\"" + content + "\"" + ")]"));
        } catch (Exception e) {
            logger.error("Нет элементов с атрибутом: " + nameAttribute + " и значением: " + content);
            throw new Exception("Нет элементов с атрибутом: " + nameAttribute + " и значением: " + content);
        }
        return element;
    }

    /**
     * Поиск элемента по тексту
     * @param text текст
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findElementByText(String text) throws Exception {
        WebElement element = null;
        try {
            logger.info("Поиск элемента по тексту: " + text);
            element = driver.findElement(By.xpath("//*[contains(text()," + "\"" + text + "\"" + ")]"));
        } catch (Exception e) {
            logger.error("Нет элемента с текстом: " + text);
            throw new Exception("Нет элемента с текстом: " + text);
        }
        return element;
    }

    public WebElement findElementByTextWithTag(String tagName,String text) throws Exception {
        WebElement element = null;
        try {
            logger.info("Поиск элемента по тексту: " + text);
            element = driver.findElement(By.xpath("//"+tagName+"[contains(text()," + "\"" + text + "\"" + ")]"));
        } catch (Exception e) {
            logger.error("Нет элемента с текстом: " + text);
            throw new Exception("Нет элемента с текстом: " + text);
        }
        return element;
    }

    public List<WebElement> findElementsByText(String text) throws Exception {
        List<WebElement> element = null;
        try {
            logger.info("Поиск элемента по тексту: " + text);
            element = driver.findElements(By.xpath("//*[contains(text()," + "\"" + text + "\"" + ")]"));
        } catch (Exception e) {
            logger.error("Нет элемента с текстом: " + text);
            throw new Exception("Нет элемента с текстом: " + text);
        }
        return element;
    }

    /**
     * Поиск родителя по дочернему элементу по атрибуту
     * @param parentElementTag родительский тэг (например div)
     * @param nameAttribute имя дочернего элемента (например name)
     * @param content значение дочернего элемента (например login)
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findParentElementByChildByAttribute(String parentElementTag, String nameAttribute, String content) throws Exception {
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//" + parentElementTag + "[*[contains(@" + nameAttribute + "," + "\"" + content + "\"" + ")]]"));
        } catch (Exception e) {
            logger.error("Нет дочернего элемента с атрибутом: " + nameAttribute + " и значением: " + content + " у элемента " + parentElementTag);
            throw new Exception("Нет дочернего элемента с атрибутом: " + nameAttribute + " и значением: " + content + " у элемента " + parentElementTag);
        }
        return element;
    }

    public WebElement findParentElementByChildByWebElement(WebElement parentElement, String parentElementTag) throws Exception {
        WebElement element = null;
            element = parentElement.findElement(By.xpath(".//parent::"+parentElementTag));
        return element;
    }

    /**
     * Поиск родителя по дочернему элементу по тексту
     * @param parentElementTag родительский тэг (например div)
     * @param text текст дочернего элемента
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findParentElementByChildByText(String parentElementTag, String text) throws Exception {
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//" + parentElementTag + "[*[contains(text()," + "\"" + text + "\"" + ")]]"));
        } catch (Exception e) {
            logger.error("Нет дочернего элемента с текстом: " + text + " у элемента " + parentElementTag);
            throw new Exception("Нет дочернего элемента с текстом: " + text + " у элемента " + parentElementTag);
        }
        return element;
    }

    /**
     * Поиск следущего элемента от предыдущего элемента по атрибуту
     * @param early имя атрибута предыдущего элемента (например name)
     * @param content значение атрибута предыдущего элемента (например login)
     * @param tagNext тэг следующего элемента (например div)
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findNextElementByAttribute(String early, String content, String tagNext) throws Exception {
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//*[contains(@" + early + "," + "\"" + content + "\"" + ")]/following-sibling::" + tagNext + ""));
        } catch (Exception e) {
            logger.error("У элемента с атрибутом " + early + " и значением " + content + " нет следующего элемента с тэгом " + tagNext);
            throw new Exception("У элемента с атрибутом " + early + " и значением " + content + " нет следующего элемента с тэгом " + tagNext);
        }
        return element;
    }

    public List<WebElement> findNextElementsByAttribute(String early, String content, String tagNext) throws Exception {
        List<WebElement> element = null;
        try {
            element = driver.findElements(By.xpath("//*[contains(@" + early + "," + "\"" + content + "\"" + ")]/following-sibling::" + tagNext + ""));
        } catch (Exception e) {
            logger.error("У элемента с атрибутом " + early + " и значением " + content + " нет следующих элементов с тэгом " + tagNext);
            throw new Exception("У элемента с атрибутом " + early + " и значением " + content + " нет следующих элементов с тэгом " + tagNext);
        }
        return element;
    }

    /**
     * Поиск следущего элемента от предыдущего элемента по тесту
     * @param earlyText текст предыдущего элемента
     * @param tagNext тэг следующего элемента (например div)
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findNextElementByText(String earlyText, String tagNext) throws Exception {
        WebElement element = null;
        try {
            element = driver.findElement(By.xpath("//*[contains(text()," + "\"" + earlyText + "\"" + ")]/following-sibling::" + tagNext + ""));
        } catch (Exception e) {
            logger.error("У элемента с текстом " + earlyText + " нет следующего элемента с тэгом " + tagNext);
            throw new Exception("У элемента с текстом " + earlyText + " нет следующего элемента с тэгом " + tagNext);
        }
        return element;
    }

    /**
     * Поиск дочернего элемента в web-элементе по тексту
     * @param parentElement родительский web-элемент
     * @param text текст дочернего элемента
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findChildElementForWebElementByText(WebElement parentElement, String text) throws Exception {
        WebElement webElement = null;
        try {
            webElement = parentElement.findElement(By.xpath(".//*[contains(text(),\"" + text + "\")]"));
        } catch (Exception e) {
            logger.info("У родительского элемента нет дочернего элемента с текстом " + text);
            throw new Exception("У родительского элемента нет дочернего элемента с текстом " + text);
        }
        return webElement;
    }

    public List<WebElement> findChildElementsForWebElementByText(WebElement parentElement, String text) throws Exception {
        List<WebElement> webElement = null;
        try {
            webElement = parentElement.findElements(By.xpath(".//*[contains(text(),\"" + text + "\")]"));
        } catch (Exception e) {
            logger.info("У родительского элемента нет дочернего элемента с текстом " + text);
            throw new Exception("У родительского элемента нет дочернего элемента с текстом " + text);
        }
        return webElement;
    }

    /**
     * Поиск дочернего элемента в web-элементе по xpath
     * @param parentElement родительский web-элемент
     * @param xpath xpath
     * @return web-element
     * @throws Exception элемент не найден
     */
    public WebElement findChildElementForWebElementByXpath(WebElement parentElement, String xpath) throws Exception {
        WebElement webElement = null;
        try {
            webElement = parentElement.findElement(By.xpath(xpath));
        } catch (Exception e) {
            logger.info("У родительского элемента нет дочернего элемента с xpath " + xpath);
            throw new Exception("У родительского элемента нет дочернего элемента с xpath " + xpath);
        }
        return webElement;
    }

    public List<WebElement> findChildElementsForWebElementByXpath(WebElement parentElement, String xpath) throws Exception {
        List<WebElement> webElement = new ArrayList<>();
        try {
            webElement = parentElement.findElements(By.xpath(xpath));
        } catch (Exception e) {
            logger.info("У родительского элемента нет дочерних элементов с xpath " + xpath);
            //throw new Exception("У родительского элемента нет дочерних элементов с xpath " + xpath);
            return webElement;
        }
        return webElement;
    }

    /**
     * Поиск дочернего элемента в web-элементе по атрибуту и его значению
     * @param parentElement родительский web-элемент
     * @param attributeName имя атрибута
     * @param context значение атрибута
     * @return web-элемент
     * @throws Exception элемент не найден
     */
    public WebElement findChildElementForWebElementByAttribute(WebElement parentElement, String attributeName, String context) throws Exception {
        WebElement webElement = null;
        try {
            webElement = parentElement.findElement(By.xpath(".//*[contains(@" + attributeName + ",\"" + context + "\")]"));
        } catch (Exception e) {
            logger.info("У родительского элемента нет дочернего элемента с атрибутом " + attributeName + " и со значением " + context);
            throw new Exception("У родительского элемента нет дочернего элемента с атрибутом " + attributeName + " и со значением " + context);
        }
        return webElement;
    }

    public List<WebElement> findChildElementsForWebElementByAttribute(WebElement parentElement, String attributeName, String context) throws Exception {
        List<WebElement> webElement = new ArrayList<>();;
        try {
            webElement = parentElement.findElements(By.xpath(".//*[contains(@" + attributeName + ",\"" + context + "\")]"));
        } catch (Exception e) {
            logger.info("У родительского элемента нет дочернего элемента с атрибутом " + attributeName + " и со значением " + context);
            return webElement;
        }
        return webElement;
    }

    /**
     * Поиск элемента по тэгу
     * @param tagName тэг
     * @return web-элемент
     * @throws Exception элемент не найден
     */
    public WebElement findElementByTagName(String tagName) throws Exception {
        WebElement webElement = null;
        try {
            webElement = driver.findElement(By.tagName(tagName));
        } catch (Exception e) {
            throw new Exception("Нет элемента с тэгом " + tagName);
        }
        return webElement;
    }

    public List<WebElement> findElementsByTagName(String tagName) throws Exception {
        List<WebElement> webElement = new ArrayList<>();
        try {
            webElement = driver.findElements(By.tagName(tagName));
        } catch (Exception e) {
            throw new Exception("Нет элемента с тэгом " + tagName);
        }
        return webElement;
    }

    /**
     * Прокрутка страницы до элемента
     * @param element web-элемент
     * @throws InterruptedException ошибка
     */
    public void scrollToElement(WebElement element) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        Thread.sleep(500);
    }

    /**
     * Поиск элемента по xpath
     * @param xpath xpath элемента
     * @return web-элемент
     * @throws Exception элемент не найден
     */
    public WebElement findElementByXpath(String xpath) throws Exception {
        WebElement webElement = null;
        try {
            webElement = driver.findElement(By.xpath(xpath));
        } catch (Exception e) {
            throw new Exception("Нет элемента с таким xpath " + xpath);
        }
        return webElement;
    }

    /**
     * Поиск элементов по xpath
     * @param xpath xpath элементов
     * @return web-элемент
     * @throws Exception элемент не найден
     */
    public List<WebElement> findElementsByXpath(String xpath) throws Exception {
        List<WebElement> webElement = new ArrayList<>();
        try {
            webElement = driver.findElements(By.xpath(xpath));
        } catch (Exception e) {
            throw new Exception("Нет элемента с таким xpath " + xpath);
        }
        return webElement;
    }

    /**
     * Прокрутка вниз страницы с ожиданиям загрузки в 2 секунды
     */
    public void scrollToDownPage() {
        JavascriptExecutor jse = (JavascriptExecutor) driver; // (driver is your browser webdriver object)
        while (true) {
            Object lastHeight = jse.executeScript("return document.body.scrollHeight");
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Object newHeight = jse.executeScript("return document.body.scrollHeight");
            if (lastHeight.equals(newHeight))
                break;
        }

    }

    /**
     * Получения URL текущей страницы
     * @return URL страницы
     */
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Выбор элемента из выподающего списка
     * @param name имя выпадающего списка
     * @param block web-элемента блока с выпадающим списком
     * @param select элемент который необходимо выбрать
     * @throws Exception элемент не найден
     */
    @Step("{0}")
    public void dropDownListByWebElement (String name,WebElement block,String select) throws Exception {
        try {
            WebElement fieldClass = findChildElementForWebElementByAttribute(block,"class","Select-value");
            fieldClass.click();
            //List<WebElement> allType = block.findElements(By.xpath(".//div[contains(@class,\"Select-option\")]"));
            List<WebElement> allType = findChildElementsForWebElementByXpath(block,".//div[contains(@class,\"Select-option\")]");
                    List<String> number = new ArrayList<>();
            for (WebElement el:allType){
                number.add(el.getText());
            }
            if (!number.toString().contains(select))
                Assert.fail("Нет элемента "+select);

            for (int i=0;i<=number.size()-1;i++){
                if (number.get(i).equals(select)) {
                    screnshoter.embedScreenshot(name);
                    allType.get(i).click();
                }
            }
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Выбор элемента из выподающего списка
     * @param name имя выпадающего списка
     * @param block web-элемента блока с выпадающим списком
     * @param select элемент который необходимо выбрать
     * @throws Exception элемент не найден
     */
    @Step("{0}")
    public void dropDownListByWebElementContains (String name,WebElement block,String select) throws Exception {
        try {
            WebElement fieldClass = findChildElementForWebElementByXpath(block, ".//*[contains(@class,\"Select-value\")]|.//*[contains(@class,\"Select-placeholder\")]");
            fieldClass.click();

        }
        catch (Exception e){
            throw e;
        }
        List<WebElement> allType = null;
        try {
            allType = findChildElementsForWebElementByXpath(block, ".//div[contains(@class,\"Select-option\")]");
        }
        catch (Exception e) {
            Assert.fail("Нет значений в выпадающем списке "+name);
        }
        try{
            List<String> number = new ArrayList<>();
            for (WebElement el:allType){
                number.add(el.getText());
            }
            if (!number.toString().contains(select))
                Assert.fail("Нет элемента "+select);
            for (int i=0;i<=number.size()-1;i++){
                if (number.get(i).contains(select)) {
                    screnshoter.embedScreenshot(name);
                    allType.get(i).click();
                    return;
                }
            }
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Поиск выпадающего списка по label и выбор элемента в нем по тексту
     * @param label label выпадающего списка
     * @param select элемент который необходимо выбрать
     * @throws Exception элемент не найден
     */
    @Step("Выбор {1} из выпадающего списка {0}")
    public void dropDownListByLabel (String label,String select) throws Exception {
        try {
            WebElement block = findElementByXpath("//label[contains(text(),\""+label+"\")]//parent::div");
            WebElement fieldClass = findChildElementForWebElementByAttribute(block,"class","Select-value");
            fieldClass.click();
            List<WebElement> allType = findChildElementsForWebElementByXpath(block,".//div[contains(@class,\"Select-option\")]");
            List<String> number = new ArrayList<>();
            for (WebElement el:allType){
                number.add(el.getText());
            }
            if (!number.toString().contains(select))
                Assert.fail("Нет элемента "+select);

            for (int i=0;i<=number.size()-1;i++){
                if (number.get(i).equals(select)) {
                    screnshoter.embedScreenshot(label);
                    allType.get(i).click();
                }
            }
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Нажатие на элеменет javascript
     * @param element web-элемент
     */
    public void clickJS (WebElement element){
        try{
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            executor.executeScript("arguments[0].click();", element);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Выбор элемента из select по тексту
     * @param selectElem web-элемент select (выпадающий список)
     * @param name элемент который необходимо выбрать
     */
    public void selectElement(WebElement selectElem, String name){
        try {
            selectElem.click();
            Select select = new Select(selectElem);
            List<WebElement> options = select.getOptions();
            List<String> values= new ArrayList<>();
            for (WebElement element:options){
                values.add(element.getText());
            }
            if (!values.toString().contains(name))
                Assert.fail("В выпадающем списке нет такого значения "+name);
            select.selectByVisibleText(name);
        }catch (Exception e){
            throw e;
        }
    }

    public void selectElementContains (WebElement selectElem, String name) throws Exception {
        WebElement element = null;
        selectElem.click();
        try {
            element = findChildElementForWebElementByText(selectElem,name);

        }catch (Exception e){
            throw new Exception("В выпадающем списке нет такого значения "+name);
        }
        element.click();
    }


    public void selectElemetByIndex (WebElement selectElem,int index){
        try {
            Select select = new Select(selectElem);
            List options = select.getOptions();
            select.selectByIndex(index);
        }catch (Exception e){
            throw e;
        }
    }

    public void selectElemetLast (WebElement selectElem){
        try {
            Select select = new Select(selectElem);
            List options = select.getOptions();
            select.selectByIndex(select.getOptions().size()-1);
        }catch (Exception e){
            throw e;
        }
    }

    public void selectElemetByValue (WebElement selectElem,String value){
        try {
            Select select = new Select(selectElem);
            List options = select.getOptions();
            select.selectByValue(value);
        }catch (Exception e){
            throw e;
        }
    }

    public String getSelectedElement (WebElement selectElement){
        Select select = new Select(selectElement);
        List options = select.getOptions();
        return select.getAllSelectedOptions().get(0).getText();
    }

    public void pageStatus () {
        try {
            URL url = new URL(driver.getCurrentUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int code = connection.getResponseCode();
            if (!(code == 200)) {
                Assert.fail("Page status " + code);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void allertYES (){
        try{
            driver.switchTo().alert().accept();
        }
        catch (Exception e){
            throw e;
        }
    }

    public void allertNO (){
        try{
            driver.switchTo().alert().dismiss();
        }
        catch (Exception e){
            throw e;
        }
    }
    public void allertGetText (){
        try{
            driver.switchTo().alert().getText();
        }
        catch (Exception e){
            throw e;
        }
    }

    public void allertSendKeys (String text){
        try{
            driver.switchTo().alert().sendKeys(text);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void checkboxChecking (WebElement webElement){
        if (!webElement.isSelected() )
        {
            webElement.click();
        }
    }

    public void switchToFrame (WebElement webElement) {
        try {
          driver.switchTo().frame(webElement);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void switchToFrame (String id) {
        try {
            driver.switchTo().frame(id);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void switchToParentFrame () {
        try {
            driver.switchTo().parentFrame();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void switchLastWindow (){
        String lastWindow = "";
        try{
            List<String> newWindow = driver.getWindowHandles().stream().collect(Collectors.toList());
            int number = 0;
            if (newWindow.size()==0)
                number=0;
            else number=newWindow.size()-1;
            lastWindow = newWindow.get(number);
        }
        catch (Exception e){
            throw e;
        }
        try{
            driver.switchTo().window(lastWindow);
        }
        catch (Exception e){
            throw e;
        }
    }

    public void switchFirstWindow (){
        try{
            List<String> newWindow = driver.getWindowHandles().stream().collect(Collectors.toList());
            driver.switchTo().window(newWindow.get(0));
        }
        catch (Exception e){
            throw e;
        }
    }

    public void newTab () {
        try{
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("window.open();");
        }
        catch (Exception e){
            throw e;
        }
    }

    public void preloadRaketa() throws Exception {
        int rocketLoader;
        int progressbarLoader=0;
        int attempt = 1;
        int count = 350;
        boolean react = true;
        Thread.sleep(1000);
        logger.info("Ожидаю исчезновения элемента "+count+" раз в течении 1 секунды");
        while (attempt!=count)
        {
            Thread.sleep(1000);
            logger.info("Попытка № "+attempt);
            try{
                driver.manage().timeouts().implicitlyWait(1,TimeUnit.SECONDS);
                WebElement visibleElement = findElementByXpath("//div[contains(@class,\"rocket-loader\")]");
                if (visibleElement.getSize().width==0&visibleElement.getSize().height==0){
                    logger.info("Элемент rocket-loader исчез");
                    rocketLoader=0;
                    react = false;
                }
                else {
                    rocketLoader=1;
                    react = true;
                }
            }
            catch (Exception e){
                logger.info("Элемента rocket-loader нет");
                rocketLoader=0;
                react = false;
            }
            try {
                if (react == false) {
                    driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                    List<WebElement> progressbar = null;
                    progressbar = findElementsByXpath("//div[contains(@id,\"progressbar\")]");
                    int counts = 0;
                    for (int i = 0; i <= progressbar.size() - 1; i++) {
                        if (!progressbar.get(i).isDisplayed()) {
                            counts++;
                        }
                    }
                    if (progressbar.size() == counts) {
                        logger.info("Элементы progressbar исчезли");
                        progressbarLoader = 0;
                    } else progressbarLoader = 1;

                }
            }
            catch (Exception e1){
                logger.info("Элементов progressbar нет");
                progressbarLoader=0;
            }
            if (rocketLoader==0&progressbarLoader==0){
                driver.manage().timeouts().implicitlyWait(Env.WAIT_ELEMENT, TimeUnit.SECONDS);
                return;
            }
            attempt++;
        }
        if (attempt ==count){
            Assert.fail("Прелоадер ракеты не исчез по истечению 350 секунд");
        }

    }

    public String getValueElement (WebElement element){
        if (element == null)
            return null;
        return element.getAttribute("value");
    }

    public void selectDropDownReact (String content) throws Exception {
        WebElement dropdown = findElementByXpath("//div[contains(@role,'listbox')][contains(@class,'open')]|//div[contains(@class,'open')]");
        WebElement element = findChildElementForWebElementByText(dropdown,content);
        element.click();
    }

}
