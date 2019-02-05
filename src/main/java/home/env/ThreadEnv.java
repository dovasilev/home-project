package home.env;

import org.openqa.selenium.WebDriver;
import home.TestData.TestingData;
import home.core.DriverFactory;
import home.core.Screnshoter;
import home.db.ConnectionMySQL;
import home.db.Database;
import home.helpers.*;
import java.util.HashMap;

public class ThreadEnv {
    private static HashMap<Thread, ThreadEnv> byThread = new HashMap<>();
    private Env env;
    private ConnectionMySQL con;
    private WebDriver driver;
    private Database db;
    private Screnshoter screnshoter;
    private CustomAssertion customAssertion;
    private Helpers helpers;
    private TableSearcher tableSearcher;
    private JSWait jsWait;
    private SoapExecution soapExecution;
    private TestingData testingData;

    public ThreadEnv(Env env) {
        System.setProperty("mail.mime.decodetext.strict", "false");
        this.env = env;
        db = new Database(env.getDbURL());
        this.con = new ConnectionMySQL();
        driver = DriverFactory.newDriver(env);
        screnshoter = new Screnshoter(driver);
        customAssertion = new CustomAssertion();
        helpers = new Helpers(driver);
        tableSearcher = new TableSearcher(driver);
        jsWait = new JSWait(driver);
        soapExecution = new SoapExecution();
        testingData = new TestingData();

    }

    public Helpers getHelpers (){
        return helpers;
    }

    public TableSearcher getTableSearcher() {
        return tableSearcher;
    }

    public JSWait getJsWait() {
        return jsWait;
    }

    public static synchronized ThreadEnv getByThread() {
        if (!hasByThread()) {
            throw new RuntimeException();
        }
        return byThread.get(Thread.currentThread());
    }

    public static synchronized void setByThread(ThreadEnv threadEnv) {
        if (hasByThread()) throw new RuntimeException();
        byThread.put(Thread.currentThread(), threadEnv);
    }

    public static synchronized void removeByThread() {
        if (!hasByThread()) throw new RuntimeException();
        byThread.remove(Thread.currentThread());
    }

    public static synchronized boolean hasByThread() {
        return byThread.containsKey(Thread.currentThread());
    }

    public static Env env() {
        return getByThread().getEnv();
    }

    public static ConnectionMySQL con() {
        return getByThread().getCon();
    }

    public static WebDriver driver() {
        return getByThread().getDriver();
    }

    public static Database db() {
        return getByThread().getDb();
    }

    public static Screnshoter screnshoter() {
        return getByThread().getScrenshoter();
    }

    public Env getEnv() {
        return env;
    }

    public ConnectionMySQL getCon() {
        return con;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public Database getDb() {
        return db;
    }

    public Screnshoter getScrenshoter() {
        return screnshoter;
    }

    public CustomAssertion getCustomAssertion() {
        return customAssertion;
    }

    public SoapExecution getSoapExecution() {
        return soapExecution;
    }

    public TestingData getTestingData() {
        return this.testingData;
    }

}
