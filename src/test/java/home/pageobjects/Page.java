package home.pageobjects;

import org.apache.log4j.Logger;
import home.TestData.TestingData;
import home.core.Screnshoter;
import home.db.ConnectionMySQL;
import home.env.ThreadEnv;
import home.helpers.*;

public class Page {
    public Logger logger;
    public ConnectionMySQL db;
    public Screnshoter screnshoter;
    public CustomAssertion assertion;
    public Helpers helpers;
    public TableSearcher tableSearcher;
    public JSWait jsWait;
    public SoapExecution soapExecution;
    public TestingData testingData;

    public Page() {
        this.logger = Logger.getLogger(this.getClass());
        this.helpers=ThreadEnv.getByThread().getHelpers();
        this.tableSearcher = ThreadEnv.getByThread().getTableSearcher();
        this.jsWait=ThreadEnv.getByThread().getJsWait();
        this.db = ThreadEnv.getByThread().getCon();
        this.assertion = ThreadEnv.getByThread().getCustomAssertion();
        this.screnshoter = ThreadEnv.getByThread().getScrenshoter();
        this.soapExecution = ThreadEnv.getByThread().getSoapExecution();
        this.testingData = ThreadEnv.getByThread().getTestingData();
    }


}
