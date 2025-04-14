package com.standingash.framework.core.factory.impl;

import com.standingash.framework.core.BeanContainer;
import com.standingash.framework.core.factory.AbstractBeanFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public class ConstructorBasedBeanFactory extends AbstractBeanFactory {

    public ConstructorBasedBeanFactory(BeanContainer beanContainer) {
        super(beanContainer);
    }

    @Override
    protected Object[] resolveDependencies(Class<?> beanClass, Set<Class<?>> visiting) {
        Constructor<?> constructor = Arrays.stream(beanClass.getDeclaredConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("No constructor found for " + beanClass));

        return Arrays.stream(constructor.getParameterTypes())
                .map(param -> this.createBean(param, visiting)).toArray();
    }

    @Override
    protected Object createBeanInstance(Class<?> beanClass, Object[] dependencies) {
        try {
            Constructor<?> constructor = Arrays.stream(beanClass.getDeclaredConstructors())
                    .max(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElseThrow(() -> new RuntimeException("No constructor found for " + beanClass));
            constructor.setAccessible(true);
            return constructor.newInstance(dependencies);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean instance: " + beanClass, e);
        }
    }
}
