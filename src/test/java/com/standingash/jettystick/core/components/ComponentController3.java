package com.standingash.jettystick.core.components;

import com.standingash.jettystick.core.annotations.Component;

@Component
public class ComponentController3 {

    private final ComponentService componentService;

    public ComponentController3(ComponentService componentService) {
        this.componentService = componentService;
    }

    public ComponentService getService() {
        return componentService;
    }
}
