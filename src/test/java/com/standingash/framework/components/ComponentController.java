package com.standingash.framework.components;

import com.standingash.framework.core.annotations.Autowired;
import com.standingash.framework.core.annotations.Component;

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
