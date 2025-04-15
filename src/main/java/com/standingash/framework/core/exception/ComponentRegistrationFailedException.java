package com.standingash.framework.core.exception;

public class ComponentRegistrationFailedException extends RuntimeException {

    private final String beanName;

    public ComponentRegistrationFailedException(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getMessage() {
        return "Failed to register component: " + beanName;
    }
}
