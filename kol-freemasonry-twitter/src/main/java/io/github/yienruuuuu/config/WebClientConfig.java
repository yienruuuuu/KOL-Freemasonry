package io.github.yienruuuuu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Eric.Lee
 * Date: 2025/4/21
 */
@Configuration
public class WebClientConfig {
    private static final String TOKEN = "";

    @Bean
    public WebClient TwitterClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                .baseUrl("https://api.x.com/2")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
