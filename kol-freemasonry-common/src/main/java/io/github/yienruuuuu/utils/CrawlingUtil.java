package io.github.yienruuuuu.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eric.Lee
 * Date: 2025/4/30
 */
@Slf4j
public class CrawlingUtil {
    private CrawlingUtil() {
        // 防止透過反射呼叫私有建構函數
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 暫停一段隨機5~15秒時間，以模擬真實請求。
     */
    public static void pauseBetweenRequests(int minSeconds, int maxSeconds) {
        int randomSleepTime = ThreadLocalRandom.current().nextInt(minSeconds, maxSeconds + 1);

        try {
            log.info("暫停 {} 秒以模擬真實請求...", randomSleepTime);
            TimeUnit.SECONDS.sleep(randomSleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("請求間暫停被中斷");
        }
    }

    /**
     * 從字串中提取第一個數字相關字串（可含小數與萬、千等單位）。
     */
    public static String extractFirstNumberAsString(String text) {
        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?(?:萬|千)?)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("找不到數字: " + text);
    }
}
