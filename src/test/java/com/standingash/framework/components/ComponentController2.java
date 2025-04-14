package com.standingash.framework.components;

import com.standingash.framework.core.annotations.Autowired;
import com.standingash.framework.core.annotations.Component;

@Component
public class ComponentController2 {

    @Autowired
    private ComponentService componentService;

    public ComponentService getService() {
        return componentService;
    }
}
