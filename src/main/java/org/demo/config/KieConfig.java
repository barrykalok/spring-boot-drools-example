package org.demo.config;

import org.kie.api.KieServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by barry.wong
 */
@Configuration
public class KieConfig {

    private KieServices kieServices = KieServices.Factory.get();

    @Bean
    public KieServices getKieServices() {
        return kieServices;
    }
}
