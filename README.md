# Jetty Stick
---
Jetty Stick is a Spring-like DI & Web Framework for Java.

### Goal:

To provide a lightweight Java Web framework.

### Developed with:

- Java 17
- Jetty 12

## Installation
---

## Getting Started
---

### Simple Example:

```java
import com.standingash.jettystick.*;

@Controller
public class MyController {
    
    private final MyService myService;
    
    public MyController myController(MyService myService) {
        this.myService = myService;
    }
    
    @RequestMapping("/service")
    public String service() {
        return myService.hello();
    }
}
```

### Note:
Field injection using the ```@Autowired``` annotation is allowed but not recommended because...

1. It might be a cause of circular dependency.
2. No ```final``` keyword means no immutability.

