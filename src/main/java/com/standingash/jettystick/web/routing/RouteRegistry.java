package com.standingash.jettystick.web.routing;

import com.standingash.jettystick.web.enums.RouteMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RouteRegistry {

    private final List<RouteHandler> handlers = new ArrayList<>();

    public void registerHandler(String path, RouteMethod routeMethod
            , Object instance, Method method) {

        String regex = path.replaceAll("\\{\\w+}", "(\\\\w+)");
        Pattern pattern = Pattern.compile(regex);

        // Path variables
        List<String> pathVariables = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{(\\w+)}").matcher(path);
        while (matcher.find())
            pathVariables.add(matcher.group(1));

        handlers.add(new RouteHandler(instance, method, pattern, pathVariables, routeMethod));
    }

    public RouteExecutionContext findExecutionContext(String path, RouteMethod method) {

        for (RouteHandler handler : handlers) {
            Matcher matcher = handler.pathPattern().matcher(path);
            if (matcher.matches() && handler.routeMethod() == method) {
                Map<String, String> pathVariables = new HashMap<>();
                for (int i = 0; i < handler.pathVariables().size(); i++)
                    pathVariables.put(handler.pathVariables().get(i), matcher.group(i + 1));
                return new RouteExecutionContext(handler, pathVariables);
            }
        }
        return null;
    }
}
