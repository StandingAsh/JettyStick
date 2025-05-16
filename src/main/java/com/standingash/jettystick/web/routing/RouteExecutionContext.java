package com.standingash.jettystick.web.routing;

import java.util.Map;

public record RouteExecutionContext(RouteHandler routeHandler, Map<String, String> pathVariables) {

    @Override
    public RouteHandler routeHandler() {
        return routeHandler;
    }

    @Override
    public Map<String, String> pathVariables() {
        return pathVariables;
    }
}
