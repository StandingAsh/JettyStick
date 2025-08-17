package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.annotations.PathVariable;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.enums.RouteMethod;

@View
public class PathVariableView {

    @Route(path = "/test1/{name}", method = RouteMethod.GET)
    public String testGet(@PathVariable("name") String name) {
        return "Hello, " + name + "!";
    }

    @Route(path = "/test2/{name}", method = RouteMethod.POST)
    public ViewResult testPost(@PathVariable("name") String name) {
        ViewContext viewContext = new ViewContext();
        viewContext.addAttribute("name", name);
        return new ViewResult("test", viewContext);
    }
}
