package io.github.yienruuuuu.impl;

import io.github.yienruuuuu.SeleniumService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Override
    public WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", chromeAddress);
        return new ChromeDriver(options);
    }
}
