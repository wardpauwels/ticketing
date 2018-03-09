package be.ward.ticketing.camundarest;

import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore({JerseyAutoConfiguration.class})
@AutoConfigureAfter({CamundaBpmAutoConfiguration.class})
public class CamundaBpmRestJerseyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CamundaJerseyResourceConfig.class)
    public CamundaJerseyResourceConfig createRestConfig() {
        return new CamundaJerseyResourceConfig();
    }
}