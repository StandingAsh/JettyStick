package com.standingash.jettystick.core.components;

import com.standingash.jettystick.core.annotations.Autowired;
import com.standingash.jettystick.core.annotations.Component;

@Component
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    public void log() {
        System.out.println("ComponentController.log");
        componentService.log();
    }

    public ComponentService getService() {
        return componentService;
    }
}
