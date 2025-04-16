package com.standingash.jettystick.web.view;

import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.service.TestService;

@View
public class TestView1 {

    private final TestService testService;

    public TestView1(TestService testService) {
        this.testService = testService;
    }

    @Route(path = "/test1")
    public String test1() {
        return testService.sayHello();
    }
}
