package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.enums.RouteMethod;

@View
public class TemplateView {

    @Route(path = "/template1", method = RouteMethod.GET)
    public ViewResult hello1() {
        ViewContext viewContext = new ViewContext();
        viewContext.addAttribute("name", "Jetty Stick");
        return new ViewResult("test", viewContext);
    }

    @Route(path = "/template2", method = RouteMethod.POST)
    public String hello2() {
        return "Hello Post";
    }
}
