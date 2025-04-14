package com.standingash.framework;

import com.standingash.framework.components.ComponentController;
import com.standingash.framework.components.ComponentController2;
import com.standingash.framework.components.ComponentController3;
import com.standingash.framework.components.ComponentService;
import com.standingash.framework.config.ConfigController;
import com.standingash.framework.config.ConfigService;
import com.standingash.framework.config.TestConfig;
import com.standingash.framework.core.ApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FrameworkTest {

    // commonly given
    private final ApplicationContext context = new ApplicationContext(
            "com.standingash.framework"
    );

    @Test
    public void testComponents() {
        // when
        ComponentController componentController = context.getBean(ComponentController.class);

        // then
        Assertions.assertNotNull(componentController);
        componentController.log();
    }

    @Test
    public void testConfiguration() {
        // when
        ConfigController configController = context.getBean(ConfigController.class);

        // then
        Assertions.assertNotNull(configController);
        configController.log();
    }

    @Test
    public void testIfSingleton() {
        // when
        ConfigController configController1 = context.getBean(ConfigController.class);
        ConfigController configController2 = context.getBean(ConfigController.class);
        ComponentController componentController1 = context.getBean(ComponentController.class);
        ComponentController componentController2 = context.getBean(ComponentController.class);
        ConfigService configService = context.getBean(ConfigService.class);
        ComponentService componentService = context.getBean(ComponentService.class);
        ComponentController2 controller2 = context.getBean(ComponentController2.class);
        ComponentController3 controller3 = context.getBean(ComponentController3.class);

        // then
        System.out.println("Checking @Bean singleton");
        Assertions.assertSame(configController1, configController2,
                "@Component not singleton.");

        System.out.println("Checking @Component singleton");
        Assertions.assertSame(componentController1, componentController2,
                "@Bean not singleton.");

        System.out.println("Checking @Autowired singleton");
        Assertions.assertSame(configService, configController1.getConfigService(),
                "injected bean not singleton.");

        System.out.println("Checking constructor injection singleton");
        Assertions.assertSame(componentService, componentController1.getService(),
                "autowired component not singleton.");

        System.out.println("Checking two different @Autowired singleton");
        Assertions.assertSame(controller2.getService(), componentController1.getService(),
                "autowired component not singleton.");

        System.out.println("Checking @Autowired and constructor injection singleton");
        Assertions.assertSame(controller2.getService(), controller3.getService(),
                "autowired component not singleton.");
    }
}
