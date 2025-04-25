package io.github.yienruuuuu.testFsm;

import org.springframework.boot.CommandLineRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

/**
 * @author Eric.Lee
 * Date: 2025/4/25
 */
@Component
public class RegisterService implements CommandLineRunner {

    private final StateMachine<RegisterState, RegisterEvent> stateMachine;

    public RegisterService(StateMachine<RegisterState, RegisterEvent> stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void run(String... args) {
        stateMachine.start();
        System.out.println("初始狀態：" + stateMachine.getState().getId());

        stateMachine.sendEvent(RegisterEvent.SUBMIT_FORM);
        System.out.println("送出表單後狀態：" + stateMachine.getState().getId());

        stateMachine.sendEvent(RegisterEvent.VERIFY_EMAIL);
        System.out.println("驗證成功後狀態：" + stateMachine.getState().getId());

        stateMachine.stop();
    }
}
