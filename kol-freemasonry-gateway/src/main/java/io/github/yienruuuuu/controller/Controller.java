package io.github.yienruuuuu.controller;

import io.github.yienruuuuu.mq.SimpleProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eric.Lee
 * Date: 2025/4/21
 */
@Slf4j
@Tag(name = "controller")
@RestController
@RequestMapping("admin")
public class Controller {

    private final SimpleProducer simpleProducer;

    public Controller(SimpleProducer simpleProducer) {
        this.simpleProducer = simpleProducer;
    }

    @Operation(summary = "測試")
    @GetMapping(value = "getPostMessenger")
    public void getPostMessenger() {
        simpleProducer.send();
    }
}
