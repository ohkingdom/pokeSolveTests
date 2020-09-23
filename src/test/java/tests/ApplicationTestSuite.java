package tests;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ApplicationTestSuite {
    private AndroidDriver<MobileElement> driver;
    private WebDriverWait wait;
    private final List<String> buttonAccessibilityIdList = Arrays.asList(
            "FireButton",
            "GrassButton",
            "WaterButton",
            "DragonButton",
            "ElectricButton",
            "SteelButton",
            "PsychicButton",
            "GhostButton");
    private final List<String> buttonIdList = Arrays.asList(
            "com.popbeans.plant:id/button_select_fire",
            "com.popbeans.plant:id/button_select_grass",
            "com.popbeans.plant:id/button_select_water",
            "com.popbeans.plant:id/button_select_dragon",
            "com.popbeans.plant:id/button_select_electric",
            "com.popbeans.plant:id/button_select_steel",
            "com.popbeans.plant:id/button_select_psychic",
            "com.popbeans.plant:id/button_select_ghost");

    @BeforeSuite
    public void setUp() throws IOException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("appActivity", ".ChoiceActivity");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("ensureWebviewsHavePages", true);
        desiredCapabilities.setCapability("app", "C:/Users/mcraed/Projects/pokeSolve/app/build/outputs/apk/debug/app-debug.apk");
        URL remoteUrl = new URL("http://localhost:4723/wd/hub");
        driver = new AndroidDriver<>(remoteUrl, desiredCapabilities);
        wait = new WebDriverWait(driver, 10);
    }

    @AfterSuite
    public void tearDown() {
        driver.quit();
    }

    @Test(priority = 1)
    public void assertPresenceOfChoices() {
        for (String s : buttonIdList) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(s)));
        }
    }

    @Test(priority = 2)
    public void validateChoices() {
        for (String s : buttonIdList) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(s))).click();
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert();
            driver.findElementById("android:id/button1").click();
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.popbeans.plant:id/button_evolve")));
            driver.navigate().back();
        }
    }

    @Test(priority = 3)
    public void endToEnd() {
        // Choose Starter Pokemon Randomly
        driver.findElementByAccessibilityId(buttonAccessibilityIdList.get(new Random().nextInt(buttonAccessibilityIdList.size()))).click();

        // Switch Context to the Modal Dialog
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();

        // Set Pokemon Nickname
        driver.findElementByAccessibilityId("NameInputField").sendKeys("Appium Test");
        driver.findElementById("android:id/button1").click();

        waitMainActivity();

        for (int n = 0; n < 3; n++) {
            waitMainActivity();

            // Get Current and Maximum Values for Loop
            int i = Integer.parseInt(driver.findElementByAccessibilityId("ValueSunCurrent").getText());
            int ii = Integer.parseInt(driver.findElementByAccessibilityId("ValueSunMaximum").getText());
            int j = Integer.parseInt(driver.findElementByAccessibilityId("ValueWaterCurrent").getText());
            int jj = Integer.parseInt(driver.findElementByAccessibilityId("ValueWaterMaximum").getText());
            int k = Integer.parseInt(driver.findElementByAccessibilityId("ValueLoveCurrent").getText());
            int kk = Integer.parseInt(driver.findElementByAccessibilityId("ValueLoveMaximum").getText());

            // Sun Value Loop
            while (i != ii) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.popbeans.plant:id/button_sun"))).click();
                wait.until(ExpectedConditions.alertIsPresent());
                driver.switchTo().alert();
                driver.findElementByAccessibilityId("VariableField").sendKeys(mathSolver(Integer.parseInt(driver.findElementByAccessibilityId("ConstantField").getText()),
                                driver.findElementByAccessibilityId("OperatorField").getText(),
                                Integer.parseInt(driver.findElementByAccessibilityId("SumField").getText())));
                driver.findElementById("android:id/button1").click();
                i++;
            }

            // Water Value Loop
            while (j != jj) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.popbeans.plant:id/button_water"))).click();
                wait.until(ExpectedConditions.alertIsPresent());
                driver.switchTo().alert();
                driver.findElementByAccessibilityId("VariableField").sendKeys(mathSolver(Integer.parseInt(driver.findElementByAccessibilityId("ConstantField").getText()),
                                driver.findElementByAccessibilityId("OperatorField").getText(),
                                Integer.parseInt(driver.findElementByAccessibilityId("SumField").getText())));
                driver.findElementById("android:id/button1").click();
                j++;
            }

            // Love Value Loop
            while (k != kk) {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.popbeans.plant:id/button_love"))).click();
                wait.until(ExpectedConditions.alertIsPresent());
                driver.switchTo().alert();
                driver.findElementByAccessibilityId("VariableField").sendKeys(mathSolver(Integer.parseInt(driver.findElementByAccessibilityId("ConstantField").getText()),
                                driver.findElementByAccessibilityId("OperatorField").getText(),
                                Integer.parseInt(driver.findElementByAccessibilityId("SumField").getText())));
                driver.findElementById("android:id/button1").click();
                k++;
            }

            // Evolve
            if (n != 2) {
                waitMainActivity();
                driver.findElementByAccessibilityId("EvolveButton").click();
                sleep(2000);
            }
        }
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        sleep(5000);
    }

    private String mathSolver(int constant, String operator, int sum) {
        return String.valueOf(Math.abs(constant - sum));
    }

    private void waitMainActivity() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.popbeans.plant:id/button_evolve")));
        } catch (NullPointerException e) {
            System.out.println("Appium was unable to find the locator within .MainActivity, and has timed out after 10 seconds.");
        }
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}