package com.standingash.jettystick.core.scanners;

import com.standingash.jettystick.core.annotations.Component;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class ComponentScanner {

    // filters annotations themselves. e.g. @View, @Service
    public static Set<Class<?>> scan(String basePackage) {

        Set<Class<?>> components = new HashSet<>();
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan()) {

            for (ClassInfo classInfo: scanResult.getAllClasses()) {
                if (!classInfo.isAnnotation()) {
                    Class<?> clazz = classInfo.loadClass();
                    if (isComponent(clazz))
                        components.add(clazz);
                }
            }
        }
        return components;
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
