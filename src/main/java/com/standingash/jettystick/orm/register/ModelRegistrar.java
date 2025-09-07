package com.standingash.jettystick.orm.register;

import com.standingash.jettystick.orm.ModelManager;
import com.standingash.jettystick.orm.ModelProxy;
import com.standingash.jettystick.orm.annotations.Model;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ModelRegistrar {

    private static final Logger log = LoggerFactory.getLogger(ModelRegistrar.class);

    public static void registerModels(String basePackage) {

        try (ScanResult scanResult = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo()
                .acceptPackages(basePackage)
                .scan()) {

            scanResult.getClassesWithAnnotation(Model.class.getName())
                    .forEach(classInfo -> {
                        try {
                            Class<?> modelClass = classInfo.loadClass();

                            @SuppressWarnings("unchecked")
                            ModelManager<Object> manager = new ModelManager<>((Class<Object>) modelClass);
                            Field objectsField = modelClass.getDeclaredField("repository");
                            objectsField.setAccessible(true);

                            @SuppressWarnings("unchecked")
                            Class<Object> managerInterface = (Class<Object>) objectsField.getType();
                            Object managerProxy = ModelProxy.createManager(
                                    managerInterface,
                                    manager
                            );
                            objectsField.set(null, managerProxy);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            log.error("Failed to register model : {}", classInfo.getName(), e);
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
