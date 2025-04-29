package io.github.yienruuuuu.impl;

import io.github.yienruuuuu.SeleniumService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Eric.Lee
 * Date: 2025/4/23
 */
@Service("seleniumService")
public class SeleniumServiceImpl implements SeleniumService {

    @Value("${selenium.chrome.address}")
    private String chromeAddress;
    @Value("${webdriver.chrome.path}")
    private String chromeDriverPath;
    private static final String URL = "https://x.com/connectfarm1/status/1916895490635080017";
    private static final String MESSAGE_URL = "https://x.com/messages";

    /**
     * 獲取WebDriver實例，
     * 因應未來程式運行在docker上，但headless chrome容易被反bot的情況下，
     * 支持headed模式下連上預先開啟的chrome。
     */
    @Override
    public WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", chromeAddress);
        return new ChromeDriver(options);
    }

    @Override
    public void checkUserHasValidInTwitter() {
        WebDriver driver = getDriver();
        driver.get(URL);

        try {
            // 等到至少有一篇文章出現
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("article")));

            // 找出所有推文 article
            List<WebElement> articles = driver.findElements(By.tagName("article"));
            System.out.println("找到推文數量：" + articles.size());

            for (WebElement article : articles) {
                try {
                    // 抓使用者名稱
                    WebElement usernameSpan = article.findElement(By.xpath(".//div[@data-testid='User-Name']//span[contains(text(), '@')]"));
                    String username = usernameSpan.getText();

                    // 抓推文內容
                    WebElement tweetTextSpan = article.findElement(By.xpath(".//div[@data-testid='tweetText']//span"));
                    String tweetContent = tweetTextSpan.getText();

                    System.out.println("使用者：" + username);
                    System.out.println("推文內容：" + tweetContent);
                    System.out.println("--------");

                } catch (NoSuchElementException e) {
                    // 如果這篇推文沒有正常資料，跳過
                    System.out.println("此篇 article 資料不完整，略過。");
                }
            }
        } catch (Exception e) {
            System.out.println("整體處理失敗：" + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
