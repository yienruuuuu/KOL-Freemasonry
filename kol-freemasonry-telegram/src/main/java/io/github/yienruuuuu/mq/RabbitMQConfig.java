package io.github.yienruuuuu.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Eric.Lee
 * Date: 2025/5/9
 */
@Configuration
public class RabbitMQConfig {
    public static final String TEST_QUEUE = "test.queue";
    public static final String DLX_QUEUE = "test.queue.dlx";
    public static final String DLX_EXCHANGE = "dead-letter-exchange";

    // 正常佇列，帶有 TTL、最大長度與 DLX
    @Bean
    public Queue testQueue() {
        return QueueBuilder.durable(TEST_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE) // 	無法處理的訊息轉送至 dead-letter-exchange
                .withArgument("x-dead-letter-routing-key", DLX_QUEUE) // 	無法處理的訊息轉送至 dead-letter-routing-key
                .withArgument("x-max-length", 1000) // 若佇列超過 1000 則，會刪除最舊的訊息（FIFO）
                .withArgument("x-message-ttl", 60000) // 訊息 60 秒內未被消費，就會轉送到死信佇列
                .build();
    }

    // 死信佇列
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLX_QUEUE).build();
    }

    // 死信交換器
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    // 綁定 DLX Queue <-> Exchange
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLX_QUEUE);
    }
}
