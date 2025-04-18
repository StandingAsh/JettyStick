package com.standingash.jettystick.app;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.web.exceptions.ServerStartFailedException;
import jakarta.servlet.http.HttpServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class ServerRunner {

    private final Server server;

    public ServerRunner(int port, HttpServlet dispatcherServlet) {

        // sets Jetty server
        server = new Server(port);

        // sets context handler
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        // registers DispatcherServlet
        handler.addServlet(new ServletHolder(dispatcherServlet), "/*");
    }

    public void start() {

        // runs Jetty server
        try{
            server.start();
            System.out.println("Server started at http://localhost:"
                    + ((ServerConnector) server.getConnectors()[0]).getLocalPort());
            server.join();
        } catch (Exception e) {
            throw new ServerStartFailedException(e.getMessage());
        }
    }
}
