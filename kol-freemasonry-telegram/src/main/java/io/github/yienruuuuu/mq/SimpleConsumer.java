package io.github.yienruuuuu.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Eric.Lee
 * Date: 2025/5/9
 */
@Component
public class SimpleConsumer {

    @RabbitListener(queues = RabbitMQConfig.TEST_QUEUE)
    public void listen(String message) {
        System.out.println("接收到訊息: " + message);
    }
}
