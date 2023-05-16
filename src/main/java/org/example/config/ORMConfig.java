package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ORMConfig {
    public ORMConfig() {
    }

    @Bean
    public boolean ormSwitch() {
        return false;
    }
}
