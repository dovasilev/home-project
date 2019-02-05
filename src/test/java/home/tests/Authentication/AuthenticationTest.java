package home.tests.Authentication;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import home.base.TestBase;
import home.env.Env;

@Epic("Аутенфикация")
public class AuthenticationTest extends TestBase {

    @Override
    public String getUrl(Env env) {
        return env.getUiURL();
    }

    @Severity(SeverityLevel.NORMAL)
    @TmsLink("")
    @Test(description = "")
    public void loginRU() throws Exception {
        //Шаги
    }

}
