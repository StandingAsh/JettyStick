package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.web.controller.TestController1;
import com.standingash.jettystick.web.controller.TestController2;
import com.standingash.jettystick.web.service.TestService;
import jakarta.servlet.Servlet;
import org.apache.hc.client5.http.fluent.Request;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class WebTest {

    static Server server;
    static int PORT;
    static String BASE_URL = "http://localhost:";
    static final String BASE_PACKAGE = "com.standingash.jettystick";
    static ApplicationContext context = new ApplicationContext(BASE_PACKAGE);

    @BeforeAll
    public static void startServer() throws Exception {

        server = new Server(0);

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        Servlet dispatcherServlet = new DispatcherServlet(context, BASE_PACKAGE);
        handler.addServlet(new ServletHolder(dispatcherServlet), "/*");

        server.setHandler(handler);
        server.start();

        PORT = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
        BASE_URL = BASE_URL + PORT;
    }

    @AfterAll
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void testComponents() throws Exception {
        TestService service = context.getBean(TestService.class);
        TestController1 controller1 = context.getBean(TestController1.class);
        TestController2 controller2 = context.getBean(TestController2.class);
        Assertions.assertNotNull(service);
        Assertions.assertNotNull(controller1);
        Assertions.assertNotNull(controller2);
        Assertions.assertEquals(controller1.test1(), controller2.test2());
    }

    @Test
    public void test1() throws IOException {
        TestService service = context.getBean(TestService.class);

        String response1 = Request.get(BASE_URL + "/test1")
                .execute().returnContent().asString();
        Assertions.assertEquals(response1, service.sayHello());
    }

    @Test
    public void test2() throws IOException {
        TestService service = context.getBean(TestService.class);

        String response2 = Request.get(BASE_URL + "/test2")
                .execute().returnContent().asString();
        Assertions.assertEquals(response2, service.sayHello());
    }
}
