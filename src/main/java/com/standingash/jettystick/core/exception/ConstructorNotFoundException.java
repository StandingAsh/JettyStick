package com.standingash.jettystick.core.exception;

public class ConstructorNotFoundException extends RuntimeException {

    private final String beanName;

    public ConstructorNotFoundException(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getMessage() {
        return "No constructor found for: " + beanName;
    }
}
