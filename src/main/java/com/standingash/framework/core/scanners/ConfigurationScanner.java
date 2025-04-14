package com.standingash.framework.core.scanners;

import com.standingash.framework.core.annotations.Configuration;
import org.reflections.Reflections;

import java.util.Set;

public class ConfigurationScanner {

    // scans all @Configuration in the base package
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Configuration.class);
    }
}
