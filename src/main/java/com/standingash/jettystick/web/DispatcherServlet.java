package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.web.annotations.PathVariable;
import com.standingash.jettystick.web.annotations.View;
import com.standingash.jettystick.web.annotations.Route;
import com.standingash.jettystick.web.enums.RouteMethod;
import com.standingash.jettystick.web.routing.RouteRegistry;
import com.standingash.jettystick.web.routing.RouteExecutionContext;
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

    private final RouteRegistry routeRegistry;
    private final TemplateEngine templateEngine;

    public DispatcherServlet(ApplicationContext context, String templateRoot) {

        this.routeRegistry = new RouteRegistry();
        this.templateEngine = new TemplateEngine(templateRoot);

        for (Class<?> viewClass : context.getBeans()) {
            if (!viewClass.isAnnotationPresent(View.class))
                continue;

            // Registers handlers for each @Route method
            Object viewInstance = context.getBean(viewClass);
            for (Method method : viewClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Route.class)) {
                    Route route = method.getAnnotation(Route.class);
                    String path = route.path();
                    RouteMethod httpMethod = route.method();
                    routeRegistry.registerHandler(path, httpMethod, viewInstance, method);
                }
            }
        }
    }

    public DispatcherServlet(ApplicationContext context) {
        this(context, "src/main/resources/templates");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        RouteMethod routeMethod = RouteMethod.valueOf(req.getMethod().toUpperCase());
        RouteExecutionContext executionContext =
                routeRegistry.findExecutionContext(path, routeMethod);

        if (executionContext == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("404 Not Found");
            return;
        }

        try {
            Object[] arguments = resolveMethodArguments(
                    executionContext.routeHandler().method(), executionContext.pathVariables()
            );
            Object result = executionContext.routeHandler().method()
                    .invoke(executionContext.routeHandler().viewInstance(), arguments);
            resp.setStatus(HttpServletResponse.SC_OK);

            // Renders HTML template
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

    // Handle path variables
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
