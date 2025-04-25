package io.github.yienruuuuu.testFsm;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

/**
 * @author Eric.Lee
 * Date: 2025/4/25
 */
@Configuration
@EnableStateMachine
public class RegisterStateMachineConfig extends StateMachineConfigurerAdapter<RegisterState, RegisterEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<RegisterState, RegisterEvent> states) throws Exception {
        states.withStates()
                .initial(RegisterState.INIT)
                .state(RegisterState.FORM_SUBMITTED)
                .end(RegisterState.VERIFIED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<RegisterState, RegisterEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(RegisterState.INIT)
                .target(RegisterState.FORM_SUBMITTED)
                .event(RegisterEvent.SUBMIT_FORM)
                .and()
                .withExternal()
                .source(RegisterState.FORM_SUBMITTED)
                .target(RegisterState.VERIFIED)
                .event(RegisterEvent.VERIFY_EMAIL);
    }
}
