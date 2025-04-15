package com.standingash.framework.core.exception;

public class MethodNotFoundException extends RuntimeException {

    private final String beanName;

    public MethodNotFoundException(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public String getMessage() {
        return "@Bean method not found for: " + beanName;
    }
}
