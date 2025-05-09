package io.github.yienruuuuu;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Eric.Lee
 * Date: 2025/4/29
 */
@Component
public class CommandRunner implements CommandLineRunner {
    private final SeleniumService seleniumService;

    public CommandRunner(SeleniumService seleniumService) {
        this.seleniumService = seleniumService;
    }

    @Override
    public void run(String... args){
//        seleniumService.checkUserHasTweet();
    }
}
