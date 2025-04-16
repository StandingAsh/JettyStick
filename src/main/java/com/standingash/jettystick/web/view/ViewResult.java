package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.model.Model;

public class ViewResult {

    private final String viewName;
    private final Model model;

    public ViewResult(String viewName, Model model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }

    public Model getModel() {
        return model;
    }
}
