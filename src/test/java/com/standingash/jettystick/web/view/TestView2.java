package com.standingash.jettystick.web.view;

import com.standingash.jettystick.core.annotations.Autowired;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.service.TestService;

@View
public class TestView2 {

    @Autowired
    private TestService testService;

    @Route(path = "/test2")
    public String test2() {
        return testService.sayHello();
    }
}
