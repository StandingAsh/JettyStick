package com.standingash.framework.core.factory;

import com.standingash.framework.core.BeanContainer;
import com.standingash.framework.core.exception.CircularDependencyException;

import java.util.Set;

public abstract class AbstractBeanFactory implements BeanFactory {

    protected final BeanContainer beanContainer;

    protected AbstractBeanFactory(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
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
    @Override
    public final Object createBean(Class<?> beanClass, Set<Class<?>> visiting) {

        if (beanContainer.contains(beanClass))
            return beanContainer.getBean(beanClass);

        if (visiting.contains(beanClass))
            throw new CircularDependencyException(beanClass.getName());
        visiting.add(beanClass);

        Object[] dependencies = resolveDependencies(beanClass, visiting);
        Object bean = createBeanInstance(beanClass, dependencies);

        beanContainer.registerBean(beanClass, bean);
        return bean;
    }

    protected abstract Object[] resolveDependencies(Class<?> beanClass, Set<Class<?>> visiting);
    protected abstract Object createBeanInstance(Class<?> beanClass, Object[] dependencies);
}
