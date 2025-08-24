package com.standingash.jettystick.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModelManager<T> {

    private static final Logger log = LoggerFactory.getLogger(ModelManager.class);
    private final List<T> storage = new ArrayList<>();

    // CRUD methods
    public void save(T model) {
        storage.add(model);
    }

    public List<T> all() {
        return new ArrayList<>(storage);
    }

    public void delete(T model) {
        storage.remove(model);
    }

    public List<T> filter(String fieldName, Object value) {
        return storage.stream()
                .filter(model -> matches(model, fieldName, value))
                .collect(Collectors.toList());
    }

    public List<T> filterComposite(List<String> fieldNames, List<String> operators, Object[] values) {
        return storage.stream()
                .filter(entity -> {
                    boolean result = matches(entity, fieldNames.get(0), values[0]);

                    for (int i = 1; i < fieldNames.size(); i++) {
                        boolean next = matches(entity, fieldNames.get(i), values[i]);
                        String op = operators.get(i - 1);

                        if ("And".equals(op)) {
                            result = result && next;
                        } else if ("Or".equals(op)) {
                            result = result || next;
                        }
                    }
                    return result;
                })
                .collect(Collectors.toList());
    }

    private boolean matches(T model, String fieldName, Object expectedValue) {

        try {
            Field field = model.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object actualValue = field.get(model);
            return Objects.equals(expectedValue, actualValue);
        } catch (NoSuchFieldException e) {
            log.error("Field {} not found", fieldName, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("Field {} not accessible", fieldName, e);
            throw new RuntimeException(e);
        }
    }
}
