package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.core.scanners.ComponentScanner;
import com.standingash.jettystick.web.annotations.Controller;
import com.standingash.jettystick.web.annotations.RequestMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private final Map<String, HandlerMethod> handlers = new HashMap<>();

    public DispatcherServlet(ApplicationContext context, String basePackage) {

        for (Class<?> controllerClass: ComponentScanner.scan(basePackage)) {
            if (!controllerClass.isAnnotationPresent(Controller.class))
                continue;

            Object controllerInstance = context.getBean(controllerClass);
            for (Method method: controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String path = method.getAnnotation(RequestMapping.class).value();
                    handlers.put(path, new HandlerMethod(controllerInstance, method));
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String path = req.getRequestURI();
        HandlerMethod handler = handlers.get(path);

        if (handler == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("404 Not Found");
            return;
        }

        try {
            Object result = handler.method.invoke(handler.instance);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            resp.getWriter().write(result.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("500 Internal Server Error");
        }
    }

    // static inner class
    private static class HandlerMethod {
        Object instance;
        Method method;

        HandlerMethod(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }
    }
}
