package com.standingash.jettystick.web.view;

import java.util.HashMap;
import java.util.Map;

public class ViewContext {

    private final Map<String, Object> attributes = new HashMap<>();

    public void addAttribute(final String key, final Object value) {
        attributes.put(key, value);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
