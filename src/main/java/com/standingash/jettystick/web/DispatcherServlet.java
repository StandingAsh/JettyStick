package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.core.scanners.ComponentScanner;
import com.standingash.jettystick.web.annotations.PathVariable;
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
import java.lang.reflect.Parameter;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private final HandlerContainer handlerContainer;
    private final TemplateEngine templateEngine;

    public DispatcherServlet(ApplicationContext context, String basePackage, String templateRoot) {

        this.handlerContainer = new HandlerContainer();
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
                    handlerContainer.registerHandler(path, httpMethod, viewInstance, method);
                }
            }
        }
    }

    public DispatcherServlet(ApplicationContext context, String basePackage) {
        this(context, basePackage, "src/main/resources/templates");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        RouteMethod routeMethod = RouteMethod.valueOf(req.getMethod());
        HandlerExecution execution = handlerContainer.findHandler(path, routeMethod);

        if (execution == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("404 Not Found");
            return;
        }

        try {
            Object[] arguments = resolveMethodArguments(execution.handlerRecord().method(),
                    execution.pathVariables());
            Object result = execution.handlerRecord().method()
                    .invoke(execution.handlerRecord().viewInstance(), arguments);

            resp.setStatus(HttpServletResponse.SC_OK);
            if (result instanceof ViewResult viewResult) {
                String renderedHtml = templateEngine
                        .render(viewResult.templateName(), viewResult.model());
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

    private Object[] resolveMethodArguments(Method method,
                                            Map<String, String> pathVariableMap) {

        Parameter[] parameters = method.getParameters();
        Object[] arguments = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariable = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariable != null)
                arguments[i] = pathVariableMap.get(pathVariable.value());
        }
        return arguments;
    }
}
