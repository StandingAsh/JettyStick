package com.standingash.jettystick.web.view;

public record ViewResult(String templateName, ViewContext viewContext) {

    public String templateName() {
        return templateName;
    }

    public ViewContext viewContext() {
        return viewContext;
    }
}
