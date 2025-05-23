package com.standingash.jettystick.core;

import com.standingash.jettystick.core.annotations.Autowired;
import com.standingash.jettystick.core.exception.ComponentRegistrationFailedException;
import com.standingash.jettystick.core.exception.DependencyInjectionFailedException;
import com.standingash.jettystick.core.factory.impl.ConstructorBasedBeanFactory;

import java.lang.reflect.Field;
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
                Object beanInstance = new ConstructorBasedBeanFactory(this)
                        .createBean(component, new HashSet<>());
                registerBean(component, beanInstance);
            } catch (Exception e) {
                throw new ComponentRegistrationFailedException(component.getName());
            }
        }
        injectDependencies();
    }

    public boolean contains(Class<?> beanClass) {
        return beans.containsKey(beanClass);
    }

    public <T> T getBean(Class<T> tClass) {
        return tClass.cast(beans.get(tClass));
    }

    public Set<Class<?>> getAllBeans() {
        return beans.keySet();
    }

    // injects dependencies for autowired field
    private void injectDependencies() {

        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Object dependency = beans.get(field.getType());
                    field.setAccessible(true);
                    try {
                        field.set(bean, dependency);
                    } catch (IllegalAccessException e) {
                        throw new DependencyInjectionFailedException(field.getName());
                    }
                }
            }
        }
    }
}
