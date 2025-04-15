package com.standingash.jettystick.core.exception;

public class MethodInvokeFailedException extends RuntimeException {

    private final String methodName;

    public MethodInvokeFailedException(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String getMessage() {
        return "Failed to invoke @Bean method: " + methodName;
    }
}
