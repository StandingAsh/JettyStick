package com.standingash.framework.core;

import java.util.List;
import java.util.Set;

public class ApplicationContext {

    private final BeanContainer beanContainer = new BeanContainer();

    public ApplicationContext(String basePackage, List<Class<?>> configClasses) {

        // handle configuration classes
        for (Class<?> configClass : configClasses) {
            ConfigurationProcessor.process(configClass, beanContainer);
        }

        // scan components and inject dependencies
        Set<Class<?>> components = ComponentScanner.scan(basePackage);
        beanContainer.registerComponents(components);
        beanContainer.injectDependencies();
    }

    public <T> T getBean(Class<T> tClass) {
        return beanContainer.getBean(tClass);
    }
}
