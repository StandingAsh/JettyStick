package com.standingash.jettystick.web;

import com.standingash.jettystick.core.ApplicationContext;
import com.standingash.jettystick.web.view.TestView1;
import com.standingash.jettystick.web.view.TestView2;
import com.standingash.jettystick.web.service.TestService;
import jakarta.servlet.Servlet;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
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
    static final String TEMPLATE_ROOT = "src/test/resources/templates";
    static ApplicationContext context = new ApplicationContext(BASE_PACKAGE);

    @BeforeAll
    public static void startServer() throws Exception {

        server = new Server(8080);

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/");
        Servlet dispatcherServlet = new DispatcherServlet(context, BASE_PACKAGE, TEMPLATE_ROOT);
        handler.addServlet(new ServletHolder(dispatcherServlet), "/*");

        server.setHandler(handler);
        server.start();

        PORT = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
        System.out.println("Server started on port " + PORT);
        BASE_URL = BASE_URL + PORT;
    }

    @AfterAll
    public static void stopServer() throws Exception {
        server.stop();
    }

    @Test
    public void testComponents() throws Exception {
        TestService service = context.getBean(TestService.class);
        TestView1 view1 = context.getBean(TestView1.class);
        TestView2 view2 = context.getBean(TestView2.class);
        Assertions.assertNotNull(service);
        Assertions.assertNotNull(view1);
        Assertions.assertNotNull(view2);
        Assertions.assertEquals(view1.test1(), view2.test2());
    }

    @Test
    public void testView1() throws IOException {
        TestService service = context.getBean(TestService.class);

        String response1 = Request.get(BASE_URL + "/test1")
                .execute().returnContent().asString();
        Assertions.assertEquals(response1, service.sayHello());
    }

    @Test
    public void testView2() throws IOException {
        TestService service = context.getBean(TestService.class);

        String response2 = Request.get(BASE_URL + "/test2")
                .execute().returnContent().asString();
        Assertions.assertEquals(response2, service.sayHello());
    }

    @Test
    public void testTemplate1() throws IOException {
        String html = Request.get(BASE_URL + "/template1")
                .execute().returnContent().asString();
        Assertions.assertTrue(html.contains("Jetty Stick"),
                "Rendered Template for view test1 not including correct name.");
    }

    @Test
    public void testTemplate2() throws IOException {
        String response = Request.post(BASE_URL + "/template2")
                .bodyString("", ContentType.DEFAULT_TEXT)
                .execute().returnContent().asString();
        Assertions.assertEquals("Hello Post", response,
                "Rendered Template for view test2 not including correct name.");
    }

    @Test
    public void testPathVariables() throws IOException {
        String name = "StandingAsh";
        String response = Request.get(BASE_URL + "/test1/" + name)
                .execute().returnContent().asString();
        System.out.println(response);
        Assertions.assertEquals("Hello, " + name + "!", response);

        String html = Request.post(BASE_URL + "/test2/" + name)
                .execute().returnContent().asString();
        System.out.println(html);
        Assertions.assertTrue(html.contains(name),
                "Rendered Template for PathVariable Post not including correct name.");
    }
}
