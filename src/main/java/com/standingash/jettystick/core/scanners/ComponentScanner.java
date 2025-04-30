package com.standingash.jettystick.core.scanners;

import com.standingash.jettystick.core.annotations.Component;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    // filters annotations themselves. e.g. @View, @Service
    public static Set<Class<?>> scan(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Component.class).stream()
                .filter(clazz -> !clazz.isAnnotation())
                .filter(ComponentScanner::isComponent)
                .collect(Collectors.toSet());
    }

    private static boolean isComponent(Class<?> clazz) {

        // directly annotated with @Component
        if (clazz.isAnnotationPresent(Component.class))
            return true;

        // checks for meta-annotation
        for (Annotation annotation : clazz.getAnnotations())
            if (annotation.annotationType().isAnnotationPresent(Component.class))
                return true;

        return false;
    }
}
