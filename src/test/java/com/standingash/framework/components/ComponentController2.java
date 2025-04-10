package com.standingash.framework.components;

import com.standingash.framework.annotations.Autowired;
import com.standingash.framework.annotations.Component;

@Component
public class ComponentController2 {

    @Autowired
    private ComponentService componentService;

    public ComponentService getService() {
        return componentService;
    }
}
