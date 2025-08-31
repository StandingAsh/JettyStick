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
                        return handleFindBy(methodName, args, manager);
                    }

                    throw new UnsupportedOperationException("Unsupported method: " + methodName);
                }
        );
    }

    private static <T> Object handleFindBy(String methodName, Object[] args, ModelManager<T> manager) {

        String conditionPart = methodName.substring(6);

        List<String> fieldNames = new ArrayList<>();
        List<String> operators = new ArrayList<>();

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
        if (fieldNames.size() == 1) {
            return manager.filter(fieldNames.get(0), args[0]);
        }
        return manager.filterComposite(fieldNames, operators, args);
    }

    private static String decapitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
}
