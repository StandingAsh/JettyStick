package com.standingash.jettystick.core;

import com.standingash.jettystick.core.annotations.Bean;
import com.standingash.jettystick.core.annotations.Configuration;
import com.standingash.jettystick.core.exception.MethodNotFoundException;
import com.standingash.jettystick.core.factory.impl.MethodBasedBeanFactory;

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
                for (Method method : configClass.getDeclaredMethods())
                    if (method.isAnnotationPresent(Bean.class))
                        beanMethods.put(method.getReturnType(), method);

                // for each @Bean create and register bean
                for (Class<?> beanType : beanMethods.keySet()) {
                    Method beanMethod = beanMethods.get(beanType);
                    if (beanMethod == null)
                        throw new MethodNotFoundException(beanType.getName());

                    new MethodBasedBeanFactory(beanContainer, configInstance, beanMethod, beanMethods)
                            .createBean(beanType, new HashSet<>());
                }
            } catch (Exception e) {
                throw new RuntimeException("Error while processing configuration", e);
            }
        }
    }
}
