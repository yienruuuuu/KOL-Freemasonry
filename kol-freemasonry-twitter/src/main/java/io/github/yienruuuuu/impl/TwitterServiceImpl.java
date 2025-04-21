package io.github.yienruuuuu.impl;

import io.github.yienruuuuu.TwitterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Eric.Lee
 * Date: 2025/4/21
 */
@Component
@Slf4j
public class TwitterServiceImpl implements TwitterService {

    private final WebClient webClient;

    public TwitterServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void getPostMessenger() {
        Mono<?> res = webClient.get()
                .uri("/tweets/")
                .retrieve()
                .bodyToMono(String.class);
        log.info("res: {}", res.block());
    }
}
