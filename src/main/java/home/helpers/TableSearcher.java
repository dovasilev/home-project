package home.helpers;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TableSearcher{
    private WebDriver driver;

    public TableSearcher(WebDriver driver) {
        this.driver = driver;
    }


    public WebElement findTableByAttribute (String nameAtt, String contentAtt) throws Exception {
        WebElement table = null;
        try {
            table = driver.findElement(By.xpath("//table[contains(@"+nameAtt+",\""+contentAtt+"\")][.//thead][.//tbody]"));
            return table;
        }
        catch (Exception e){
            throw new Exception("Нет таблицы с именем атрибута "+nameAtt+" и значением "+contentAtt);
        }
    }

    public WebElement findTrInTableByText (WebElement table,String text){
        WebElement tr = null;
        try {
            tr = table.findElement(By.xpath(".//tr[.//*[contains(text(),\""+text+"\")]]"));
            return tr;
        }
        catch (Exception e){
            throw e;
        }
    }

    public List<WebElement> findTdInTableByThead (WebElement table, String thead) {
        List<WebElement> td = null;
        try{
            List<WebElement> th = table.findElements(By.xpath(".//th"));
            WebElement contains = table.findElement(By.xpath(".//th[contains(text(),\""+thead+"\")]"));
            for (int i=0;i<=th.size()-1;i++){
                if (th.get(i).equals(contains)){
                    td = table.findElements(By.xpath(".//tbody/tr/td["+(i+1)+"]"));
                }
            }
            return td;
        }
        catch (Exception e){
            throw e;
        }
    }

    public WebElement findTdInTrByThead (WebElement tr,String thead) {
        WebElement td = null;
        try {
            List<WebElement> th = tr.findElement(By.xpath(".//ancestor::table")).findElements(By.xpath(".//th"));
            WebElement contains = tr.findElement(By.xpath(".//ancestor::table")).findElement(By.xpath(".//th[contains(text(),\""+thead+"\")]"));
            for (int i=0;i<=th.size()-1;i++){
                if (th.get(i).equals(contains)){
                    td = tr.findElement(By.xpath(".//td["+(i+1)+"]"));
                }
            }
            return td;
        }
        catch (Exception e){
            throw e;
        }
    }
    
}
