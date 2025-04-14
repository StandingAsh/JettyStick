package com.standingash.framework.components;

import com.standingash.framework.core.annotations.Component;

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
