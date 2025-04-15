package com.standingash.jettystick.core.exception;

public class BeanCreationFailedException extends RuntimeException {

    private final String beanName;

    public BeanCreationFailedException(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getMessage() {
        return "Failed to create bean instance for: " + beanName;
    }
}
