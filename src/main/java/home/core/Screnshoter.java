package home.core;

import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import home.listeners.ScreenshotListener;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class Screnshoter {
    private static final Logger logger = Logger.getLogger(ScreenshotListener.class);
    public WebDriver driver;

    public Screnshoter(WebDriver driver) {
        this.driver = driver;
    }

    public boolean createFile(File screenshot) {
        boolean fileCreated = false;

        if (screenshot.exists()) {
            fileCreated = true;
        } else {
            File parentDirectory = new File(screenshot.getParent());
            if (parentDirectory.exists() || parentDirectory.mkdirs()) {
                try {
                    fileCreated = screenshot.createNewFile();
                } catch (IOException errorCreatingScreenshot) {
                    errorCreatingScreenshot.printStackTrace();
                }
            }
        }

        return fileCreated;
    }

    public void writeScreenshotToFile(String name, File screenshot,boolean fullPage) {
        try {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            if (fullPage)
            screenshotStream.write(takeScreenShotFullPage(name));
            else screenshotStream.write(takeScreenShot(name));

            screenshotStream.close();
        } catch (IOException unableToWriteScreenshot) {
            logger.error("Unable to write " + screenshot.getAbsolutePath());
            unableToWriteScreenshot.printStackTrace();
        }
    }

    public void writeScreenshotToFileAlert(String name, File screenshot) {
        try {
            FileOutputStream screenshotStream = new FileOutputStream(screenshot);
            screenshotStream.write(takeScreenshot(name));
            screenshotStream.close();
        } catch (IOException unableToWriteScreenshot) {
            logger.error("Unable to write " + screenshot.getAbsolutePath());
            unableToWriteScreenshot.printStackTrace();
        }
    }

    @Attachment(type = "image/png", value = "{0}")
    public byte[] takeScreenShot(@SuppressWarnings("unused") String name) throws IOException {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(type = "image/png", value = "{0}")
    public byte[] takeScreenShotFullPage(@SuppressWarnings("unused") String name) throws IOException {
        final Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(driver);
        return toByteArrayAutoClosable(screenshot.getImage(),"png");
    }

    @Attachment(type = "image/png", value = "{0}")
    public byte[] takeScreenshot(@SuppressWarnings("unused") String name) throws IOException{
        BufferedImage image;
        byte[] imageInByte = null;
        try {

            image = new Robot().createScreenCapture(new Rectangle(
                    Toolkit.getDefaultToolkit().getScreenSize()));
            imageInByte = toByteArrayAutoClosable(image, "png");
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return imageInByte;
    }

    private static byte[] toByteArrayAutoClosable(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }

    public void embedScreenshot(String name) {
        try {
            name = name.replace("/", "");
            String screenshotDirectory = System.getProperty("screenshotDirectory", "target/screenshots");
            String screenshotAbsolutePath = screenshotDirectory + File.separator + System.currentTimeMillis() + "_" + name + ".png";
            File screenshot = new File(screenshotAbsolutePath);
            if (createFile(screenshot)) {
                try {
                    writeScreenshotToFile(name, screenshot,false);
                } catch (ClassCastException weNeedToAugmentOurDriverObject) {
                    writeScreenshotToFile(name, screenshot,false);
                }
                catch (UnhandledAlertException e){
                    writeScreenshotToFileAlert(name, screenshot);
                }
                logger.info("Written screenshot to " + screenshotAbsolutePath);
            } else {
                logger.error("Unable to create " + screenshotAbsolutePath);
            }
        } catch (Exception ex) {
            logger.error("Unable to capture screenshot...");
            ex.printStackTrace();
        }
    }

    public void embedScreenshotFullPage (String name) {
        try {
            name = name.replace("/", "");
            String screenshotDirectory = System.getProperty("screenshotDirectory", "target/screenshots");
            String screenshotAbsolutePath = screenshotDirectory + File.separator + System.currentTimeMillis() + "_" + name + ".png";
            File screenshot = new File(screenshotAbsolutePath);
            if (createFile(screenshot)) {
                try {
                    writeScreenshotToFile(name, screenshot,true);
                } catch (ClassCastException weNeedToAugmentOurDriverObject) {
                    writeScreenshotToFile(name, screenshot,true);
                }
                catch (UnhandledAlertException e){
                    writeScreenshotToFileAlert(name, screenshot);
                }
                logger.info("Written screenshot to " + screenshotAbsolutePath);
            } else {
                logger.error("Unable to create " + screenshotAbsolutePath);
            }
        } catch (Exception ex) {
            logger.error("Unable to capture screenshot...");
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    @Attachment(value = "{0}", type = "text/plain")
    public String addMap(@SuppressWarnings("unused") String name, Map<String, String> map) {
        StringBuilder resp = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            resp.
                    append(entry.getKey()).
                    append(" : ").
                    append(entry.getValue()).
                    append("\n");
        }
        return resp.toString();
    }

}
