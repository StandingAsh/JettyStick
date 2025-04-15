package com.standingash.jettystick.core.factory.impl;

import com.standingash.jettystick.core.exception.MethodInvokeFailedException;
import com.standingash.jettystick.core.BeanContainer;
import com.standingash.jettystick.core.factory.AbstractBeanFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MethodBasedBeanFactory extends AbstractBeanFactory {

    private final Object configInstance;
    private final Method beanMethod;
    private final Map<Class<?>, Method> beanMethods;

    public MethodBasedBeanFactory(BeanContainer beanContainer, Object configInstance,
                                  Method beanMethod, Map<Class<?>, Method> beanMethods) {
        super(beanContainer);
        this.configInstance = configInstance;
        this.beanMethod = beanMethod;
        this.beanMethods = beanMethods;
    }

    @Override
    protected Object[] resolveDependencies(Class<?> beanClass, Set<Class<?>> visiting) {
        return Arrays.stream(beanMethod.getParameterTypes())
                .map(param -> {
                    Method dependency = beanMethods.get(param);
                    if (dependency != null)
                        return new MethodBasedBeanFactory(
                                beanContainer, configInstance, dependency, beanMethods
                        ).createBean(param, visiting);
                    else
                        return new ConstructorBasedBeanFactory(beanContainer)
                                .createBean(param, visiting);
                }).toArray();
    }

    @Override
    protected Object createBeanInstance(Class<?> beanClass, Object[] dependencies) {
        try {
            return beanMethod.invoke(configInstance, dependencies);
        } catch (Exception e) {
            throw new MethodInvokeFailedException(beanMethod.getName());
        }
    }
}
