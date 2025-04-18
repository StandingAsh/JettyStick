package com.standingash.jettystick.web.exceptions;

public class TemplateRenderingFailedException extends RuntimeException {

    private final String templatePath;

    public TemplateRenderingFailedException(String templatePath) {
        this.templatePath = templatePath;
    }

    @Override
    public String getMessage() {
      return "Failed to render template: " + templatePath;
    }
}
