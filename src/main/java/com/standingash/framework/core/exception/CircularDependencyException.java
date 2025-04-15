package com.standingash.framework.core.exception;

public class CircularDependencyException extends RuntimeException {

    private final String beanName;

    public CircularDependencyException(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getMessage() {
        return "Circular Dependency Detected for: " + beanName;
    }
}
