package com.standingash.framework.core;

import com.standingash.framework.annotations.Autowired;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanContainer {

    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void registerBean(Class<?> beanClass, Object beanInstance) {
        beans.put(beanClass, beanInstance);
    }

    // registers components into bean
    public void registerComponents(Set<Class<?>> components) {

        for(Class<?> component : components) {
            try{
                Object beanInstance = component.getDeclaredConstructor().newInstance();
                registerBean(component, beanInstance);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
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
