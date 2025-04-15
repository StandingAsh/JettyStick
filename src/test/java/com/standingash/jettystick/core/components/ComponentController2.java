package com.standingash.jettystick.core.components;

import com.standingash.jettystick.core.annotations.Autowired;
import com.standingash.jettystick.core.annotations.Component;

@Component
public class ComponentController2 {

    @Autowired
    private ComponentService componentService;

    public ComponentService getService() {
        return componentService;
    }
}
