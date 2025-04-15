package com.standingash.jettystick.web.controller;

import com.standingash.jettystick.core.annotations.Autowired;
import com.standingash.jettystick.web.annotations.Controller;
import com.standingash.jettystick.web.annotations.RequestMapping;
import com.standingash.jettystick.web.service.TestService;

@Controller
public class TestController2 {

    @Autowired
    private TestService testService;

    @RequestMapping("/test2")
    public String test2() {
        return testService.sayHello();
    }
}
