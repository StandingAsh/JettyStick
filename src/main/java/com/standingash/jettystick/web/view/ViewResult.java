package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.model.Model;

public record ViewResult(String viewName, Model model) {

    @Override
    public String viewName() {
        return viewName;
    }

    @Override
    public Model model() {
        return model;
    }
}
