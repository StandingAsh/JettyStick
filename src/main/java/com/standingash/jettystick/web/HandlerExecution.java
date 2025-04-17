package com.standingash.jettystick.web;

import java.util.Map;

public record HandlerExecution(HandlerRecord handlerRecord, Map<String, String> pathVariables) {

    @Override
    public HandlerRecord handlerRecord() {
        return handlerRecord;
    }

    @Override
    public Map<String, String> pathVariables() {
        return pathVariables;
    }
}
