package com.standingash.framework.components;

import com.standingash.framework.annotations.Autowired;
import com.standingash.framework.annotations.Component;

@Component
public class ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    public void log() {
        System.out.println("ComponentService.log");
        componentRepository.log();
    }
}
