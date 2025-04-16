package com.standingash.jettystick.core.scanners;

import com.standingash.jettystick.core.annotations.Component;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    // filters annotations themselves. e.g. @View, @Service
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Component.class)
                .stream().filter(component -> !component.isAnnotation())
                .collect(Collectors.toSet());
    }
}
