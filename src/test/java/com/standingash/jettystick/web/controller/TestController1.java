package com.standingash.jettystick.web.controller;

import com.standingash.jettystick.web.annotations.Controller;
import com.standingash.jettystick.web.annotations.RequestMapping;
import com.standingash.jettystick.web.service.TestService;

@Controller
public class TestController1 {

    private final TestService testService;

    public TestController1(TestService testService) {
        this.testService = testService;
    }

    @RequestMapping("/test1")
    public String test1() {
        return testService.sayHello();
    }
}
