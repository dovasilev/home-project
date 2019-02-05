package home.env;

import org.apache.log4j.Logger;
import home.utils.ConfigReader;

public class Env {
    private static final Logger logger = Logger.getLogger(Env.class);

    // public static final int WAIT_EXPLICIT_SEC = Integer.parseInt(!(System.getProperty("wait.explicit.seconds") == null) ? System.getProperty("wait.explicit.seconds") : ConfigReader.loadProperty("wait.explicit.seconds"));
    public static final int WAIT_ELEMENT = Integer.parseInt(!(System.getProperty("wait.element") == null) ? System.getProperty("wait.element") : ConfigReader.loadProperty("wait.element"));
    public static final int DRIVER_SLEEP = Integer.parseInt((System.getenv("DRIVER_SLEEP") == null) ? "2" : System.getenv("DRIVER_SLEEP"));
    private String sdURL;
    private String uiURL;
    private String dbURL;

    public Env(String uiURL, String dbURL, String sdURL) throws Exception {
        this.uiURL = uiURL;
        this.dbURL = dbURL;
        this.sdURL = sdURL;


        if (this.uiURL == null) {
            logger.error("Не задана переменная UI_URL \n " +
                    "Не указана ссылка на вэб интерфейс");
            throw new Exception("Не указана ссылка на вэб интерфейс, создайте и заполните переменную UI_URL");
        } else if (this.uiURL.equals("")) {
            logger.error("Не задана переменная UI_URL \n " +
                    "Не указана ссылка на вэб интерфейс");
            throw new Exception("Не указана ссылка на вэб интерфейс, создайте и заполните переменную UI_URL");
        }
        if (this.sdURL == null) {
            logger.warn("Не задана переменная GRID_URL \n " +
                    "Тесты будут проходить локально на машине в которой собирается проект \n" +
                    "Если же браузера нет, то тесты упадут в ошибку \n");
        } else if (this.sdURL.equals("")) {
            logger.warn("Не задана переменная GRID_URL \n " +
                    "Тесты будут проходить локально на машине в которой собирается проект \n" +
                    "Если же браузера нет, то тесты упадут в ошибку \n");
        }
        if (this.dbURL == null) {
            logger.error("Не задана переменная dbURL \n " +
                    "Не указан URL connection к Базе данных");
            throw new Exception("Не указан URL connection к Базе данных, создайте и заполните переменную dbURL");
        } else if (this.dbURL.equals("")) {
            logger.warn("Не указан URL connection к Базе данных");
        }
    }

    public Env(EnvJson json) throws Exception {
        this(json.getUiURL(), json.getDbURL(), json.getSdURL());
    }

    public String getUiURL() {
        return uiURL;
    }

    public String getDbURL() {
        return dbURL;
    }

    public String getSdURL() {
        return sdURL;
    }
}