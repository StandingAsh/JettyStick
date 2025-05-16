package com.standingash.jettystick.web.routing;

import com.standingash.jettystick.web.enums.RouteMethod;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public record RouteHandler(Object viewInstance, Method method, Pattern pathPattern,
                           List<String> pathVariables, RouteMethod routeMethod) {

    @Override
    public Object viewInstance() {
        return viewInstance;
    }

    @Override
    public Method method() {
        return method;
    }

    @Override
    public Pattern pathPattern() {
        return pathPattern;
    }

    @Override
    public List<String> pathVariables() {
        return pathVariables;
    }

    @Override
    public RouteMethod routeMethod() {
        return routeMethod;
    }
}
