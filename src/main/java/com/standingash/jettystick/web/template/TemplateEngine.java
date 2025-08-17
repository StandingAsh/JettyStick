package com.standingash.jettystick.web.template;

import com.standingash.jettystick.web.exception.TemplateRenderingFailedException;
import com.standingash.jettystick.web.view.ViewContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateEngine {

    private final String basePath;
    private final Map<String, String> templateCache = new HashMap<>();

    public TemplateEngine(String basePath) {
        this.basePath = basePath;
    }

    public String render(String viewName, ViewContext viewContext) {
        try {
            String path = basePath + "/" + viewName + ".html";
            String template;

            if (templateCache.containsKey(path))
                template = templateCache.get(path);
            else {
                template = Files.readString(Paths.get(path));
                templateCache.put(path, template);
            }
            Pattern pattern = Pattern.compile("\\{\\{\\s*(\\w+)\\s*}}");
            Matcher matcher = pattern.matcher(template);

            StringBuilder result = new StringBuilder();

            while (matcher.find()) {
                String key = matcher.group(1);
                Object value = viewContext.getAttributes().getOrDefault(key, "");
                matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
            }
            matcher.appendTail(result);
            return result.toString();
        } catch (IOException e) {
            throw new TemplateRenderingFailedException(basePath);
        }
    }
}
