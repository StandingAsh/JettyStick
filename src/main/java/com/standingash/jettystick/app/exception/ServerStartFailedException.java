package com.standingash.jettystick.app.exception;

public class ServerStartFailedException extends RuntimeException {

    public ServerStartFailedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
