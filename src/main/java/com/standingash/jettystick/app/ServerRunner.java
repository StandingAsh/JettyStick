package com.standingash.jettystick.app;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.web.DispatcherServlet;
import com.standingash.jettystick.app.exception.ServerStartFailedException;
import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerRunner {

    private static final Logger log = LoggerFactory.getLogger(ServerRunner.class);
    private final Server server;

    public ServerRunner(int port, final String TEMPLATE_ROOT, ApplicationContext context) {

        // sets Jetty server
        server = new Server(port);

        // sets context handler
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        // registers DispatcherServlet
        HttpServlet dispatcherServlet = new DispatcherServlet(context, TEMPLATE_ROOT);
        handler.addServlet(new ServletHolder(dispatcherServlet), "/*");
    }

    public void start() {

        // runs Jetty server
        try{
            server.start();
            log.info("Server started at http://localhost:{}",
                    ((ServerConnector) server.getConnectors()[0]).getLocalPort());
            server.join();
        } catch (Exception e) {
            throw new ServerStartFailedException(e.getMessage());
        }
    }
}
