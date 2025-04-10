package com.standingash.framework.core;

import com.standingash.framework.annotations.Bean;
import com.standingash.framework.annotations.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ConfigurationProcessor {

    // handles all beans in the given configuration class
    public static void process(Class<?> configClass, BeanContainer beanContainer) {

        if (configClass.isAnnotationPresent(Configuration.class)) {
            try {
                Object configInstance = configClass.getDeclaredConstructor().newInstance();
                beanContainer.registerBean(configClass, configInstance);

                // stores all @Bean methods
                Map<Class<?>, Method> beanMethods = new HashMap<>();
                for (Method method : configClass.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Bean.class)) {
                        beanMethods.put(method.getReturnType(), method);
                    }
                }

                // for each @Bean create and register bean
                for (Class<?> beanType: beanMethods.keySet()) {
                    createBean(beanType, configInstance, beanMethods, beanContainer, new HashSet<>());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
       First I tried sequentially registering beans using for-each,
       turned out that this could be a problem.

       Say, a Controller with a Service dependency-injected is about to be bean-registered.
       But if the Service instance isn't registered before the Controller's turn,
       the Controller will get a null object injected because it's Service is absent.

       Spring Framework uses CGLIB proxy to deal with this plus to keep beans singleton.
       I decided to recursively register beans following the dependency-tree instead.
    */
    private static Object createBean(Class<?> beanClass, Object configInstance,
                                     Map<Class<?>, Method> beanMethods,
                                     BeanContainer beanContainer, Set<Class<?>> visiting) throws InvocationTargetException, IllegalAccessException {

        // keeps all beans singleton
        if (beanContainer.contains(beanClass))
            return beanContainer.getBean(beanClass);

        // avoids circular dependencies
        if (visiting.contains(beanClass))
            throw new RuntimeException("Circular dependency found: " + beanClass);
        visiting.add(beanClass);

        Method beanMethod = beanMethods.get(beanClass);
        if (beanMethod == null)
            throw new RuntimeException("@Bean method not found: " + beanClass);

        // recursively create bean
        Object[] parameters = Arrays.stream(beanMethod.getParameterTypes())
                .map(paramType -> {
                    try {
                        return createBean(paramType, configInstance, beanMethods, beanContainer, visiting);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toArray();

        Object bean = beanMethod.invoke(configInstance, parameters);
        beanContainer.registerBean(beanClass, bean);
        return bean;
    }
}
