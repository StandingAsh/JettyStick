package com.standingash.jettystick.web.exceptions;

public class ServerStartFailedException extends RuntimeException {

    public ServerStartFailedException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
