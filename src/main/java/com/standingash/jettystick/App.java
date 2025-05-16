package com.standingash.jettystick;

import com.standingash.jettystick.app.ServerRunner;
import com.standingash.jettystick.core.ApplicationContext;

public class App {

    public static void run(Class<?> mainClass, String[] args) {

        final int PORT = 8080;
        final String BASE_PACKAGE = mainClass.getPackageName();
        final String TEMPLATE_ROOT = "src/main/resources/templates";

        ApplicationContext context = new ApplicationContext(BASE_PACKAGE);
        ServerRunner serverRunner = new ServerRunner(PORT, TEMPLATE_ROOT, context);

        serverRunner.start();
    }
}
