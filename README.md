# Jetty Stick

Jetty Stick is a Spring-like DI & Web Framework for Java.

### Goal:

To provide a lightweight Java Web framework.

### Developed with:

- Java 17
- Jetty 12

## Installation

## Getting Started

### Simple Usage:

```java
import com.standingash.jettystick.*;

@View
public class TestView {
    
    private final TestService testService;
    
    public TestView(TestService testService) {
        this.testService = testService;
    }
    
    @Route(path = "/test", method = RouteMethod.POST)
    public String test() {
        Model model = new Model();
        model.addAttribute("name", testService.test());
        return new ViewResult("test", model);
    }
}
```

```test.html```
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Test</title>
</head>
<body>
    <h1>Hello, {{ name }}!</h1>
</body>
</html>
```

### Note:
Field injection using the ```@Autowired``` annotation is allowed but not recommended because...

1. It might be a cause of circular dependency.
2. No ```final``` keyword means no immutability.

