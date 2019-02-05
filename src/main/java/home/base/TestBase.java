package home.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import home.TestData.TestingData;
import home.core.Screnshoter;
import home.db.ConnectionMySQL;
import home.env.Env;
import home.env.ThreadEnv;
import home.helpers.*;
import home.listeners.FrontListener;
import java.util.concurrent.TimeUnit;

@Listeners(FrontListener.class)
public abstract class TestBase {
    public static final Logger logger = Logger.getLogger(TestBase.class);
    public WebDriver driver;
    public ConnectionMySQL db;
    public Screnshoter screnshoter;
    public CustomAssertion assertion;
    public JSWait jsWait;
    public SoapExecution soapExecution;
    public TestingData testingData;


    @BeforeMethod(alwaysRun = true, description = "Проверка системных переменных и инициализация драйвера")
    public void instantiateDriverObject(ITestContext ctx) throws Exception {
        try {
            String url = getUrl(ThreadEnv.env());
            driver = ThreadEnv.driver();
            driver.manage().timeouts().pageLoadTimeout(Env.WAIT_ELEMENT*5, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(Env.WAIT_ELEMENT, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(Env.WAIT_ELEMENT, TimeUnit.SECONDS);
            logger.info("Перехожу по ссылке " + url);
            driver.get(url);
            //region инициализация всех нужных классов
            this.jsWait=ThreadEnv.getByThread().getJsWait();
            this.db = ThreadEnv.getByThread().getCon();
            this.assertion = ThreadEnv.getByThread().getCustomAssertion();
            this.soapExecution = ThreadEnv.getByThread().getSoapExecution();
            this.testingData = ThreadEnv.getByThread().getTestingData();
            this.screnshoter = ThreadEnv.getByThread().getScrenshoter();
            //endregion
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        logger.info("Инициализация завершена");
    }

    /**
     * Абстрактный метод для назначения URL из переменной среды TEST_CONFIG
     * @param env класс с переменными
     * @return url пример: https://yandex.ru
     */
    public abstract String getUrl(Env env);


}
