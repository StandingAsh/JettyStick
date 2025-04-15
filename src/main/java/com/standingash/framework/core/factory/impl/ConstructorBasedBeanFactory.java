package com.standingash.framework.core.factory.impl;

import com.standingash.framework.core.BeanContainer;
import com.standingash.framework.core.exception.BeanCreationFailedException;
import com.standingash.framework.core.exception.ConstructorNotFoundException;
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
                .orElseThrow(() -> new ConstructorNotFoundException(beanClass.getName()));

        return Arrays.stream(constructor.getParameterTypes())
                .map(param -> this.createBean(param, visiting)).toArray();
    }

    @Override
    protected Object createBeanInstance(Class<?> beanClass, Object[] dependencies) {
        try {
            Constructor<?> constructor = Arrays.stream(beanClass.getDeclaredConstructors())
                    .max(Comparator.comparingInt(Constructor::getParameterCount))
                    .orElseThrow(() -> new ConstructorNotFoundException(beanClass.getName()));

            constructor.setAccessible(true);
            return constructor.newInstance(dependencies);
        } catch (Exception e) {
            throw new BeanCreationFailedException(beanClass.getName());
        }
    }
}
