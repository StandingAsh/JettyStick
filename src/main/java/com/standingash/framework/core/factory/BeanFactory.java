package com.standingash.framework.core.factory;

import java.util.Set;

public interface BeanFactory {
    Object createBean(Class<?> beanClass, Set<Class<?>> visiting);
}
