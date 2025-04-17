package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.enums.RouteMethod;
import com.standingash.jettystick.web.model.Model;

@View
public class TemplateView {

    @Route(path = "/template1", method = RouteMethod.GET)
    public ViewResult hello1() {
        Model model = new Model();
        model.addAttribute("name", "Jetty Stick");
        return new ViewResult("test", model);
    }

    @Route(path = "/template2", method = RouteMethod.POST)
    public String hello2() {
        return "Hello Post";
    }
}
