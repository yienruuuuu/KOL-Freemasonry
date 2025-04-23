package io.github.yienruuuuu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric.Lee
 * Date: 2025/4/21
 */
@Slf4j
@Tag(name = "Telegram controller")
@RestController
@RequestMapping("admin")
public class Controller {
    private final SeleniumService seleniumService;

    public Controller(SeleniumService seleniumService) {
        this.seleniumService = seleniumService;
    }


    @Operation(summary = "測試")
    @GetMapping(value = "getPostMessenger")
    public void getPostMessenger() {
        WebDriver driver = seleniumService.getDriver();
        driver.get("https://yahoo.com.tw");
    }
}
