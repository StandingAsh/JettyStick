package com.standingash.jettystick.web;

import com.standingash.jettystick.web.enums.RouteMethod;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {

    private final Map<String, Map<RouteMethod, HandlerMethod>> handlers = new HashMap<>();

    public void registerHandler(String path, RouteMethod method, HandlerMethod handler) {
        handlers.computeIfAbsent(path, k -> new HashMap<>()).put(method, handler);
    }

    public HandlerMethod getHandler(String path, RouteMethod method) {
        Map<RouteMethod, HandlerMethod> methods = handlers.get(path);
        if (methods == null)
            return null;
        return methods.get(method);
    }

    public boolean hasPath(String path) {
        return handlers.containsKey(path);
    }

    public boolean hasMethod(String path, RouteMethod method) {
        Map<RouteMethod, HandlerMethod> methods = handlers.get(path);
        return methods != null && methods.containsKey(method);
    }
}
