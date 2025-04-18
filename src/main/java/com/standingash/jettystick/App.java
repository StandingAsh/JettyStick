package com.standingash.jettystick;

import com.standingash.jettystick.app.ServerRunner;
import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.web.DispatcherServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class App {

    public static void main(String[] args) throws Exception {

        int port = 0;

        // sets DI context
        String basePackage = "com.standingash.jettystick";
        String templateRoot = "src/main/resources/templates";
        ApplicationContext context = new ApplicationContext(basePackage);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(context, basePackage, templateRoot);

        ServerRunner serverRunner = new ServerRunner(port, dispatcherServlet);
        serverRunner.start();
    }
}
