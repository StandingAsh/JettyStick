package com.standingash.jettystick.core.config;

public class ConfigService {

    private final ConfigRepository configRepository;

    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public void log() {
        System.out.println("ConfigService.log");
        configRepository.log();
    }
}
