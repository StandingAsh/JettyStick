package com.standingash.jettystick.web.annotations;

import com.standingash.jettystick.web.enums.RouteMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String path();
    RouteMethod method() default RouteMethod.GET;
}
