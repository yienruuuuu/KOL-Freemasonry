package io.github.yienruuuuu;

import org.openqa.selenium.WebDriver;

/**
 * @author Eric.Lee
 * Date: 2025/4/23
 */
public interface SeleniumService {


    /**
     * 取得WebDriver
     *
     * @return WebDriver
     */
    WebDriver getDriver();

    /**
     * 檢查使用者是否有Premium帳號
     */
    void checkUserHasValidInTwitter();

    /**
     * 檢查使用者是否有推文
     */
    void checkUserHasTweet();
}
