package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.annotations.PathVariable;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.enums.RouteMethod;

@View
public class PathVariableView {

    @Route(path = "/test/{name}", method = RouteMethod.GET)
    public String test(@PathVariable("name") String name) {
        return "Hello, " + name + "!";
    }
}
