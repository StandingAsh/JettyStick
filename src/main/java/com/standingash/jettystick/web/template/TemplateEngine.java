package com.standingash.jettystick.web.template;

import com.standingash.jettystick.web.model.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private final String basePath;

    public TemplateEngine(String basePath) {
        this.basePath = basePath;
    }

    public String render(String viewName, Model model) {
        try {
            String path = basePath + "/" + viewName + ".html";
            String template = Files.readString(Paths.get(path));

            Pattern pattern = Pattern.compile("\\{\\{\\s*(\\w+)\\s*}}");
            Matcher matcher = pattern.matcher(template);

            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String key = matcher.group(1);
                Object value = model.getAttributes().getOrDefault(key, "");
                matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
            }
            matcher.appendTail(result);
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to render template", e);
        }
    }
}
