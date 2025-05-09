package io.github.yienruuuuu.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Eric.Lee
 * Date: 2025/5/9
 */
@Component
public class SimpleProducer {

    private final RabbitTemplate rabbitTemplate;

    public SimpleProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send() {
        rabbitTemplate.convertAndSend("test.queue", "Hello from RabbitMQ!");
    }
}
