package com.standingash.framework.config;

public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    public void log() {
        System.out.println("ConfigController.log");
        configService.log();
    }

    public ConfigService getConfigService() {
        return configService;
    }
}
