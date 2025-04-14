package com.standingash.framework.core;

import com.standingash.framework.core.annotations.Bean;
import com.standingash.framework.core.annotations.Configuration;
import com.standingash.framework.core.factory.impl.MethodBasedBeanFactory;

import java.lang.reflect.Method;
import java.util.*;

public class ConfigurationProcessor {

    // handles all beans in the given configuration class
    public static void process(Class<?> configClass, BeanContainer beanContainer) {

        if (configClass.isAnnotationPresent(Configuration.class)) {
            try {
                Object configInstance = configClass.getDeclaredConstructor().newInstance();

                // stores all @Bean methods
                Map<Class<?>, Method> beanMethods = new HashMap<>();
                for (Method method : configClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        beanMethods.put(method.getReturnType(), method);
                    }
                }

                // for each @Bean create and register bean
                for (Class<?> beanType: beanMethods.keySet()) {
                    Method beanMethod = beanMethods.get(beanType);
                    if (beanMethod == null)
                        throw new RuntimeException("@Bean method not found: " + beanType);

                    new MethodBasedBeanFactory(beanContainer, configInstance, beanMethod, beanMethods)
                            .createBean(beanType, new HashSet<>());
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while processing configuration", e);
            }
        }
    }
}
