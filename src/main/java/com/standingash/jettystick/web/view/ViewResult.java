package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.model.Model;

public record ViewResult(String templateName, Model model) {

    public String templateName() {
        return templateName;
    }

    @Override
    public Model model() {
        return model;
    }
}
