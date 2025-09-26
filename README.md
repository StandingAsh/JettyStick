# Jetty Stick

Jetty Stick is an annotation-based DI & Web Framework for Java.

### Goal:

To provide a lightweight Java MVT Web framework.

### Developed with:

- Java 17
- Jetty 12

## Installation

## Getting Started

### Define a Model

```java
import com.standingash.jettystick.*;

@Model
public class User {

    private Long id;
    private String name;
    
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    
    public static UserRepository repository;

    // declare your methods here
    public interface UserRepository {
        void save(User user);
        void delete(User user);
        List<User> findAll();
        List<User> findById(Long id);
        User findByNameAndId(String name, Long id);
        List<User> findByNameOrId(String name, Long id);
    }
}
```
The @Model class is responsible for...
- storing data (entity)
- saving, deleting and searching (repository)

Declare your repository as a static variable in the @Model class. Repository methods are injected automatically.

`save` and `delete` methods are created by default.
`find` method supports...
1. `All`, `By` keywords
2. `And`, `Or` operators
3. performing either `findAll` or `findOne` depending on the return type

### Define a View

```java
import com.standingash.jettystick.*;

@View
public class UserView {
    
    private final User user;
    
    public UserView(User user) {
        this.user = user;
    }
    
    @Route(path = "/signup/{id}", method = RouteMethod.GET)
    public String signUp(@Pathvariable("id") Long id, @Pathvariable("name") String name) {
        User newUser = new User(id, name);
        user.repository.save(newUser);
        
        ViewContext viewContext = new ViewContext();
        viewContext.addAttribute("name", newUser.getName());
        return new ViewResult("test", viewContext);
    }
}
```

### Template

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

### Notes:
Field injection using the ```@Autowired``` annotation is allowed but not recommended because...

1. It might be a cause of circular dependency.
2. No ```final``` keyword means no immutability.

