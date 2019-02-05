package home.tests;

import home.pageobjects.HomePage;
import io.qameta.allure.Epic;
import io.qameta.allure.TmsLink;
import org.apache.log4j.Logger;
import home.base.TestBase;
import home.env.Env;
import org.testng.annotations.Test;


@Epic("Home test-suites")
public class HomeTest extends TestBase {
    private static final Logger logger = Logger.getLogger(HomeTest.class);

    @Override
    public String getUrl(Env env) {
        return env.getUiURL();
    }


    @TmsLink("123")
    @Test(enabled = true)
    public void searchCatInYandex(){
        HomePage homePage= new HomePage();
        homePage.searchCat("Котики");
    }

}


