package io.github.yienruuuuu.impl;

import io.github.yienruuuuu.SeleniumService;
import io.github.yienruuuuu.bean.dto.TwitterAccountDto;
import io.github.yienruuuuu.utils.CrawlingUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * @author Eric.Lee
 * Date: 2025/4/23
 */
@Service("seleniumService")
@Slf4j
public class SeleniumServiceImpl implements SeleniumService {

    @Value("${selenium.chrome.address}")
    private String chromeAddress;
    @Value("${webdriver.chrome.path}")
    private String chromeDriverPath;
    private static final String URL = "https://x.com/crypto_two_face/status/1918192560499298379";
    private static final String MESSAGE_URL = "https://x.com/messages";
    private static final String MOCK_SEARCH_MESSAGE = "hi~ What exchange do you use?";

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

    /**
     * 檢查使用者是否有Premium帳號，並取得帳戶資訊
     * 1. 前往訊息頁面
     * 2. 搜尋訊息(使用者應已經主動傳送了驗證碼)
     * 3. 點擊訊息
     * 4. 擷取使用者帳號資訊(帳號、帳號名稱、追蹤人數、是否有藍勾勾)
     */
    @Override
    public void checkUserHasValidInTwitter() {
        WebDriver driver = getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions actions = new Actions(driver);
        // 前往訊息頁面
        driver.get(MESSAGE_URL);
        try {
            // 等待頁面加載完成
            WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@placeholder='搜尋私人訊息']")
            ));
            // 模擬滑鼠靠近（hover）
            actions.moveToElement(searchInput)
                    .pause(Duration.ofMillis(500 + new Random().nextInt(500)))
                    .perform();
            // 滑鼠點擊搜尋框
            searchInput.click();
            CrawlingUtil.pauseBetweenRequests(1, 6);
            // 模擬人類輸入
            humanType(driver, MOCK_SEARCH_MESSAGE);
            CrawlingUtil.pauseBetweenRequests(1, 6);
            // 找尋正確的訊息
            WebElement msgElement = findCorrectMessage(driver, MOCK_SEARCH_MESSAGE);
            // 模擬滑鼠靠近（hover）
            actions.moveToElement(msgElement)
                    .pause(Duration.ofMillis(500 + new Random().nextInt(500)))
                    .perform();
            // 滑鼠點擊搜尋框
            msgElement.click();
            CrawlingUtil.pauseBetweenRequests(1, 6);
            // 找尋使用者帳號
            TwitterAccountDto account = findUserAccount(driver);
            log.info("使用者資訊：{}", account);
        } catch (Exception e) {
            log.error("驗證使用者註冊失敗", e);
        } finally {
            driver.quit();
        }
    }

    /**
     * 檢查使用者是否有前往任務點進行推文
     * 1. 前往推文頁面
     * 2. 等待頁面加載完成
     * 3. 找出所有推文
     * 4. 擷取使用者名稱及推文內容
     */
    @Override
    public void checkUserHasTweet() {
        //TODO 僅簡單測試 需完善下滑及比對訊息數量
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
            log.error("整體處理失敗", e);
        } finally {
            driver.quit();
        }
    }


    /**
     * 模擬人類輸入文字的方式
     */
    private void humanType(WebDriver driver, String text) throws InterruptedException {
        Actions actions = new Actions(driver);
        for (char c : text.toCharArray()) {
            actions.sendKeys(Character.toString(c)).perform();
            Thread.sleep(100 + new Random().nextInt(200));
        }
    }

    /**
     * 從搜尋結果中找尋比對相符的訊息
     */
    private WebElement findCorrectMessage(WebDriver driver, String expectedText) {
        // 找出所有 conversation 區塊
        List<WebElement> conversations = driver.findElements(By.cssSelector("[data-testid='conversation']"));

        for (WebElement conversation : conversations) {
            // 取得當前 conversation 下的所有 tweetText
            List<WebElement> tweetTexts = conversation.findElements(By.cssSelector("[data-testid='tweetText']"));

            StringBuilder actualTextBuilder = new StringBuilder();
            for (WebElement tweetText : tweetTexts) {
                // 每個 tweetText 中會有很多 span，逐一拼接
                List<WebElement> spans = tweetText.findElements(By.tagName("span"));
                for (WebElement span : spans) {
                    actualTextBuilder.append(span.getText());
                }
            }
            if (expectedText.contentEquals(actualTextBuilder)) {
                log.info("帳號驗證中，找到符合的訊息：{}", actualTextBuilder);
                return conversation;
            }
        }
        throw new NoSuchElementException("找不到匹配的訊息");
    }

    /**
     * 從主訊息框中找到使用者帳號
     */
    private TwitterAccountDto findUserAccount(WebDriver driver) {
        List<String> result = new ArrayList<>();
        // 主聊天框元素
        WebElement container = driver.findElement(By.cssSelector("[data-testid='DmScrollerContainer']"));
        // 所有對話區塊
        List<WebElement> items = container.findElements(By.cssSelector("[data-testid='cellInnerDiv']"));

        List<WebElement> spans = items.get(0).findElements(By.cssSelector("span"));
        for (WebElement span : spans) {
            String text = span.getText();
            if (text != null && !text.isEmpty()) {
                result.add(text);
            }
        }
        boolean isPremium = !items.get(0).findElements(By.cssSelector("[data-testid='icon-verified']")).isEmpty();
        log.info("帳號驗證中，使用者帳號資訊，自訊息中擷取結果：{}, Premium:{}", result, isPremium);

        // 檢查結果是否合法
        this.checkResultIsLegal(result);
        // 結果轉換為DTO並返回
        return this.parseResultToDto(result, isPremium);
    }

    /**
     * 將結果轉換為TwitterAccountDto
     */
    private TwitterAccountDto parseResultToDto(List<String> result, boolean isPremium) {
        TwitterAccountDto twitterAccountDto = new TwitterAccountDto();
        twitterAccountDto.setAccountName(result.get(0));
        twitterAccountDto.setPremium(isPremium);
        result.forEach(
                text -> {
                    if (text.contains("位跟隨者")) {
                        twitterAccountDto.setFollowersCount(CrawlingUtil.extractFirstNumberAsString(text));
                    }
                    if (text.startsWith("@")) {
                        twitterAccountDto.setAccount(text);
                    }
                }
        );
        return twitterAccountDto;
    }

    /**
     * 檢查結果是否合法
     */
    private void checkResultIsLegal(List<String> result) {
        if (result.size() < 2) {
            throw new NoSuchElementException("找不到匹配的訊息");
        }
        long atCount = result.stream().filter(text -> text.startsWith("@")).count();
        long followersCount = result.stream().filter(text -> text.contains("位跟隨者")).count();
        if (atCount != 1) {
            throw new IllegalStateException("帳號 (@開頭) 出現異常數量: " + atCount);
        }
        if (followersCount != 1) {
            throw new IllegalStateException("『位跟隨者』出現異常數量: " + followersCount);
        }
    }

}
