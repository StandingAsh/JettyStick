package com.standingash.jettystick.core;

import com.standingash.jettystick.core.scanners.ConfigurationScanner;
import com.standingash.jettystick.core.scanners.ComponentScanner;
import com.standingash.jettystick.orm.register.ModelRegistrar;

import java.util.Set;

public class ApplicationContext {

    private final BeanContainer beanContainer = new BeanContainer();

    public ApplicationContext(String basePackage) {

        // handle configuration classes
        Set<Class<?>> configClasses = ConfigurationScanner.scan(basePackage);
        for (Class<?> configClass : configClasses)
            ConfigurationProcessor.process(configClass, beanContainer);

        // scan components and inject dependencies
        Set<Class<?>> components = ComponentScanner.scan(basePackage);
        beanContainer.registerComponents(components);

        // register model managers to all models
        ModelRegistrar.registerModels(basePackage);
    }

    public <T> T getBean(Class<T> tClass) {
        return beanContainer.getBean(tClass);
    }

    public Set<Class<?>> getBeans() {
        return beanContainer.getAllBeans();
    }
}
