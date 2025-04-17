package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.core.scanners.ComponentScanner;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.enums.RouteMethod;
import com.standingash.jettystick.web.template.TemplateEngine;
import com.standingash.jettystick.web.view.ViewResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;

public class DispatcherServlet extends HttpServlet {

    private final HandlerMapping handlerMapping;
    private final TemplateEngine templateEngine;

    public DispatcherServlet(ApplicationContext context, String basePackage, String templateRoot) {
        this.handlerMapping = new HandlerMapping();
        this.templateEngine = new TemplateEngine(templateRoot);

        for (Class<?> viewClass : ComponentScanner.scan(basePackage)) {
            if (!viewClass.isAnnotationPresent(View.class))
                continue;

            Object viewInstance = context.getBean(viewClass);
            for (Method method : viewClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Route.class)) {
                    Route route = method.getAnnotation(Route.class);
                    String path = route.path();
                    RouteMethod httpMethod = route.method();
                    handlerMapping.registerHandler(path, httpMethod,
                            new HandlerMethod(viewInstance, method));
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        RouteMethod routeMethod = RouteMethod.valueOf(req.getMethod());
        HandlerMethod handler = handlerMapping.getHandler(path, routeMethod);

        if (!handlerMapping.hasPath(path)) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("404 Not Found");
            return;
        }

        if (handler == null) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            resp.getWriter().write("405 Method Not Allowed");
            return;
        }

        try {
            Object result = handler.method().invoke(handler.instance());
            resp.setStatus(HttpServletResponse.SC_OK);

            if (result instanceof ViewResult viewResult) {
                String renderedHtml = templateEngine
                        .render(viewResult.viewName(), viewResult.model());
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write(renderedHtml);
            } else {
                resp.setContentType("text/plain;charset=UTF-8");
                resp.getWriter().write(result.toString());
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("500 Internal Server Error\n");
            throw new RuntimeException(e.getMessage());
        }
    }
}
