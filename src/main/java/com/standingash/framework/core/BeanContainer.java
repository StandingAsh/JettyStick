package com.standingash.framework.core;

import com.standingash.framework.annotations.Autowired;
import com.standingash.framework.annotations.Bean;
import com.standingash.framework.annotations.Component;
import com.standingash.framework.annotations.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

    private Map<Class<?>, Object> beans = new HashMap<>();

    public void initialize(String basePackage) {

        Set<Class<?>> components = ComponentScanner.scan(basePackage);

        // handle Configuration classes
        for (Class<?> component : components) {
            if (component.isAnnotationPresent(Configuration.class)) {
                try {
                    Object configInstance = component.getDeclaredConstructor().newInstance();
                    for (Method method : component.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(Bean.class)) {
                            Object bean = method.invoke(configInstance, resolveDependencies(method));
                            beans.put(method.getReturnType(), bean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // auto-register components
        for (Class<?> component : components) {
            if (component.isAnnotationPresent(Component.class)) {
                try {
                    Object instance = component.getDeclaredConstructor().newInstance();
                    beans.put(component, instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // @Autowired
        injectDependencies();
    }

    private void injectDependencies() {

        // Autowire dependencies
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType());
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private Object[] resolveDependencies(Method method) {

        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = beans.get(parameterTypes[i]);
        }
        return parameters;
    }

    public <T> T getBean(Class<T> tClass) {
        return tClass.cast(beans.get(tClass));
    }
}
