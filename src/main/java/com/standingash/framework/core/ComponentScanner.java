package com.standingash.framework.core;

import com.standingash.framework.annotations.Component;
import org.reflections.Reflections;

import java.util.Set;

public class ComponentScanner {

    // scans all @Component in the base package
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Component.class);
    }
}
