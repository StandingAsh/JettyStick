package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.core.scanners.ComponentScanner;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.template.TemplateEngine;
import com.standingash.jettystick.web.view.ViewResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private final Map<String, Map<String, HandlerMethod>> handlers = new HashMap<>();
    private final TemplateEngine templateEngine;

    public DispatcherServlet(ApplicationContext context, String basePackage, String templateRoot) {
        this.templateEngine = new TemplateEngine(templateRoot);

        for (Class<?> viewClass : ComponentScanner.scan(basePackage)) {
            if (!viewClass.isAnnotationPresent(View.class))
                continue;

            Object viewInstance = context.getBean(viewClass);
            for (Method method : viewClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Route.class)) {
                    Route route = method.getAnnotation(Route.class);
                    String path = route.path();
                    String httpMethod = route.method().toUpperCase();
                    handlers.computeIfAbsent(path, k -> new HashMap<>())
                            .put(httpMethod, new HandlerMethod(viewInstance, method));
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        String httpMethod = req.getMethod().toUpperCase();

        Map<String, HandlerMethod> methods = handlers.get(path);
        if (methods == null || !methods.containsKey(httpMethod)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("404 Not Found");
            return;
        }

        HandlerMethod handler = methods.get(httpMethod);
        try {
            Object result = handler.method.invoke(handler.instance);

            if (result instanceof ViewResult viewResult) {
                String rendered = templateEngine
                        .render(viewResult.getViewName(), viewResult.getModel());
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write(rendered);
            } else {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("text/plain;charset=UTF-8");
                resp.getWriter().write(result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("500 Internal Server Error\n" + e.getMessage());
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
