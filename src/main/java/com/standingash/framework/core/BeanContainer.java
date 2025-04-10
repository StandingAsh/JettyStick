package com.standingash.framework.core;

import com.standingash.framework.annotations.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BeanContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void registerBean(Class<?> beanClass, Object beanInstance) {
        beans.put(beanClass, beanInstance);
    }

    // registers components into bean
    public void registerComponents(Set<Class<?>> components) {

        for (Class<?> component : components) {
            if (contains(component))
                continue;

            try {
                Object beanInstance = createInstance(component, new HashSet<>());
                registerBean(component, beanInstance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Object createInstance(Class<?> beanClass, Set<Class<?>> visiting) throws InvocationTargetException, InstantiationException, IllegalAccessException {

        if(visiting.contains(beanClass))
            throw new RuntimeException("Circular dependency found: "+beanClass);
        visiting.add(beanClass);

        // Chooses constructor with most parameters
        Constructor<?>[] constructors = beanClass.getDeclaredConstructors();
        Constructor<?> constructor = Arrays.stream(constructors)
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("No constructor found for " + beanClass));

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Object dependency = beans.get(parameterType);

            if (dependency == null) {
                dependency = createInstance(parameterType, visiting);
                registerBean(parameterType, dependency);
            }
            parameters[i] = dependency;
        }

        constructor.setAccessible(true);
        return constructor.newInstance(parameters);
    }

    // injects dependencies for autowired field
    public void injectDependencies() {

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

    public boolean contains(Class<?> beanClass) {
        return beans.containsKey(beanClass);
    }

    public <T> T getBean(Class<T> tClass) {
        return tClass.cast(beans.get(tClass));
    }
}
