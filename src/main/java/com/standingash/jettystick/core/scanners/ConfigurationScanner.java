package com.standingash.jettystick.core.scanners;

import com.standingash.jettystick.core.annotations.Configuration;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.util.HashSet;
import java.util.Set;

public class ConfigurationScanner {

    // scans all @Configuration in the base package
    public static Set<Class<?>> scan(String basePackage) {

        Set<Class<?>> configurations = new HashSet<>();
        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan()) {

            for (ClassInfo classInfo: scanResult.getClassesWithAnnotation(Configuration.class))
                if(!classInfo.isAnnotation())
                    configurations.add(classInfo.loadClass());
        }
        return configurations;
    }
}
