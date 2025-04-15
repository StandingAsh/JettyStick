package com.standingash.jettystick.core.config;

import com.standingash.jettystick.core.annotations.Bean;
import com.standingash.jettystick.core.annotations.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public ConfigRepository configRepository() {
        return new ConfigRepository();
    }

    @Bean
    public ConfigService configService(ConfigRepository configRepository) {
        return new ConfigService(configRepository);
    }

    @Bean
    public ConfigController configController(ConfigService configService) {
        return new ConfigController(configService);
    }
}
