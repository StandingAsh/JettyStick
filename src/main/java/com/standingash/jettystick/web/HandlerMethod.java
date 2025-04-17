package com.standingash.jettystick.web;

import java.lang.reflect.Method;

public record HandlerMethod(Object instance, Method method) {

    @Override
    public Object instance() {
        return instance;
    }

    @Override
    public Method method() {
        return method;
    }
}
