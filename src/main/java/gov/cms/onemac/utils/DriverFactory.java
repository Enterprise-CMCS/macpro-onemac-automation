package gov.cms.onemac.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

        public static WebDriver createDriver() {
            // Read system properties first (from GitHub Actions), fallback to ConfigReader
         /*   System.setProperty("webdriver.http.factory", "jdk-http-client");
            System.setProperty("webdriver.chrome.driver", "C:\\Users\\57901\\Downloads\\OneMAC-Automation\\OneMAC-Automation\\src\\test\\resources\\chromedriver\\chromedriver.exe");
            WebDriver driver;
            ChromeOptions chromeOptions;
            chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless=new");
            chromeOptions.addArguments("--window-size=1920,1080");
            chromeOptions.setBinary ("C:\\Users\\57901\\Downloads\\OneMAC-Automation\\OneMAC-Automation\\src\\test\\resources\\chrome-win64\\chrome.exe");
            driver = new ChromeDriver(chromeOptions);*/
            String browser = System.getProperty("browser");
            if (browser == null || browser.isEmpty()) {
                browser = ConfigReader.get("browser");
            }

            String headlessProp = System.getProperty("headless");
            boolean isHeadless = headlessProp != null ? Boolean.parseBoolean(headlessProp) :
                    Boolean.parseBoolean(ConfigReader.get("headless"));

            WebDriver driver;

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isHeadless) {
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--window-size=1920,1080");
                    } else {
                        chromeOptions.addArguments("--start-maximized");
                    }
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--no-sandbox");
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup(); // automatically downloads matching geckodriver
                    FirefoxOptions firefoxOptions = new FirefoxOptions();

                    if (isHeadless) {
                        firefoxOptions.addArguments("--headless=new"); // use new headless
                        firefoxOptions.addArguments("--width=1920");
                        firefoxOptions.addArguments("--height=1080");
                    }

                    // Critical for Linux CI
                    firefoxOptions.addArguments("--no-sandbox");
                    firefoxOptions.addArguments("--disable-dev-shm-usage");
                    firefoxOptions.addArguments("--disable-gpu"); // optional

                    driver = new FirefoxDriver(firefoxOptions);
                    if (!isHeadless) driver.manage().window().maximize();
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            return driver;
        }

}