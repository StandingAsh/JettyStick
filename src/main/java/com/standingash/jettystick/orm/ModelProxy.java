package com.standingash.jettystick.orm;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class ModelProxy {

    @SuppressWarnings("unchecked")
    public static <T> T createManager(Class<T> modelClass, ModelManager<T> manager) {

        return (T) Proxy.newProxyInstance(
                modelClass.getClassLoader(),
                new Class[]{modelClass},
                (proxy, method, args) -> {
                    String methodName = method.getName();

                    switch (methodName) {
                        case "save" -> {
                            manager.save((T) args[0]);
                            return null;
                        }
                        case "findAll" -> {
                            return manager.all();
                        }
                        case "delete" -> {
                            manager.delete((T) args[0]);
                            return null;
                        }
                    }

                    // findBy mapping
                    if (methodName.startsWith("findBy")) {
                        return handleFindBy(methodName, args, manager, method.getReturnType());
                    }

                    throw new UnsupportedOperationException("Unsupported method: " + methodName);
                }
        );
    }

    private static <T> Object handleFindBy(
            String methodName, Object[] args,
            ModelManager<T> manager, Class<?> returnType) {

        String conditionPart = methodName.substring(6);

        List<String> fieldNames = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        // handles operands(And, Or)
        StringBuilder currentCondition = new StringBuilder();
        for (int i = 0; i < conditionPart.length(); i++) {
            if (conditionPart.startsWith("And", i)) {
                fieldNames.add(decapitalize(currentCondition.toString()));
                operators.add("And");
                currentCondition.setLength(0);
                i += 2;
            } else if (conditionPart.startsWith("Or", i)) {
                fieldNames.add(decapitalize(currentCondition.toString()));
                operators.add("Or");
                currentCondition.setLength(0);
                i += 1;
            } else {
                currentCondition.append(conditionPart.charAt(i));
            }
        }
        if (!currentCondition.isEmpty()) {
            fieldNames.add(decapitalize(currentCondition.toString()));
        }

        List<T> results;
        if (fieldNames.size() == 1) {
            results = manager.filter(fieldNames.get(0), args[0]);
        } else {
            results = manager.filterComposite(fieldNames, operators, args);
        }

        // handles return type
        if (returnType.equals(manager.getType())) {
            return results.isEmpty() ? null : results.get(0);
        } else if (List.class.isAssignableFrom(returnType)) {
            return results;
        }
        // ... more to extend
        else {
            throw new UnsupportedOperationException("Unsupported return type for method: " + methodName);
        }
    }

    private static String decapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
