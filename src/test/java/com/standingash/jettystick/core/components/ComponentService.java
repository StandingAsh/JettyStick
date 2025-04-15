package com.standingash.jettystick.core.components;

import com.standingash.jettystick.core.annotations.Autowired;
import com.standingash.jettystick.core.annotations.Component;

@Component
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    public void log() {
        System.out.println("ComponentService.log");
        componentRepository.log();
    }
}
