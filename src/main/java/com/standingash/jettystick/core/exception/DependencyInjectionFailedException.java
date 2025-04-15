package com.standingash.jettystick.core.exception;

public class DependencyInjectionFailedException extends RuntimeException {

    private final String fieldName;

    public DependencyInjectionFailedException(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getMessage() {
        return "Failed to inject dependency for field: " + fieldName;
    }
}
